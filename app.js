// Task 3: Training Eligibility System

const API_BASE = 'http://localhost:8080/api/eligibility';

// 1. Training Programmes & Simple Rules
const trainings = {
  1: {
    id: 1,
    title: 'Financial Management Programme',
    rules: 'Allowed Departments: Finance, Budget, Planning',
    allowedDepts: ['Finance', 'Budget', 'Planning'],
    reqGrade: null,
    minYears: 0
  },
  2: {
    id: 2,
    title: 'Technical Programme',
    rules: 'Allowed Departments: IT, ICT',
    allowedDepts: ['IT', 'ICT'],
    reqGrade: null,
    minYears: 0
  },
  3: {
    id: 3,
    title: 'Management Development Programme',
    rules: 'Required Grade: Grade I or Grade II | Minimum 5 Years of Service',
    allowedDepts: null,
    reqGrade: ['Grade I', 'Grade II'],
    minYears: 5
  }
};

// 2. Officers List
let officers = [
  { id: 1, name: 'A. Perera', department: 'Finance', grade: 'Grade III', yearsOfService: 6, lastAttended: null },
  { id: 2, name: 'B. Silva', department: 'IT', grade: 'Grade II', yearsOfService: 6, lastAttended: { trainingId: 2, monthsAgo: 6 } },
  { id: 3, name: 'C. Fernando', department: 'Planning', grade: 'Grade I', yearsOfService: 10, lastAttended: { trainingId: 1, monthsAgo: 18 } },
  { id: 4, name: 'D. Bandara', department: 'Administration', grade: 'Grade III', yearsOfService: 2, lastAttended: null },
  { id: 5, name: 'E. Jayawardena', department: 'HR', grade: 'Grade I', yearsOfService: 8, lastAttended: { trainingId: 3, monthsAgo: 8 } },
  { id: 6, name: 'F. Gunasekara', department: 'Budget', grade: 'Grade II', yearsOfService: 5, lastAttended: null },
  { id: 7, name: 'G. Wickramasinghe', department: 'ICT', grade: 'Grade III', yearsOfService: 4, lastAttended: null }
];

let selectedTrainingId = 1;
let selectedOfficerId = 1;

// Initialize on page load
document.addEventListener('DOMContentLoaded', async () => {
  await loadOfficersFromDB();
  populateDropdown();
  updateDetails();
});

// Load Officers from Database API (if backend is running)
async function loadOfficersFromDB() {
  try {
    const res = await fetch(`${API_BASE}/officers`);
    if (res.ok) {
      const dbOfficers = await res.json();
      if (dbOfficers && dbOfficers.length > 0) {
        officers = dbOfficers;
      }
    }
  } catch (err) {
    console.log('Running with local officer list');
  }
}

// Populate Officers Dropdown
function populateDropdown() {
  const select = document.getElementById('officerSelect');
  select.innerHTML = officers.map(o => `
    <option value="${o.id}">${o.name} (${o.department} - ${o.grade}, ${o.yearsOfService} yrs)</option>
  `).join('');
}

// Event: Training Selected
function onTrainingSelect() {
  selectedTrainingId = parseInt(document.getElementById('trainingSelect').value);
  document.getElementById('ruleText').innerText = trainings[selectedTrainingId].rules;
  updateDetails();
  hideResult();
}

// Event: Officer Selected
function onOfficerSelect() {
  selectedOfficerId = parseInt(document.getElementById('officerSelect').value);
  updateDetails();
  hideResult();
}

// Update the Officer Profile Details Table
function updateDetails() {
  const officer = officers.find(o => o.id === selectedOfficerId);
  if (!officer) return;

  document.getElementById('dispDept').innerText = officer.department;
  document.getElementById('dispGrade').innerText = officer.grade;
  document.getElementById('dispService').innerText = officer.yearsOfService + ' Years';

  // Check previous participation
  const partEl = document.getElementById('dispParticipation');
  if (officer.lastAttended && officer.lastAttended.trainingId === selectedTrainingId) {
    const months = officer.lastAttended.monthsAgo;
    if (months < 12) {
      partEl.innerHTML = `<span style="color:#dc2626; font-weight:bold;">Attended ${months} months ago (Within 12 months)</span>`;
    } else {
      partEl.innerHTML = `<span style="color:#16a34a;">Attended ${months} months ago (&gt; 12 months)</span>`;
    }
  } else {
    partEl.innerHTML = `<span style="color:#16a34a;">No participation within last 12 months</span>`;
  }
}

// Check Eligibility Action
async function handleCheck(e) {
  e.preventDefault();

  const training = trainings[selectedTrainingId];
  const officer = officers.find(o => o.id === selectedOfficerId);
  if (!training || !officer) return;

  // 1. Try Backend Spring Boot API
  try {
    const res = await fetch(`${API_BASE}/check`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        trainingId: training.id,
        officerId: officer.id
      })
    });

    if (res.ok) {
      const data = await res.json();
      showResult(data.eligible, data.message, data.reason);
      return;
    }
  } catch (err) {
    console.log('Backend not connected, using simple local check');
  }

  // 2. Simple Local Check Fallback
  const result = checkLocally(training, officer);
  showResult(result.eligible, result.message, result.reason);
}

// Local evaluation logic
function checkLocally(training, officer) {
  // 1. Department Check
  if (training.allowedDepts && !training.allowedDepts.includes(officer.department)) {
    return {
      eligible: false,
      message: 'Officer is not eligible for this training programme.',
      reason: `Officer belongs to ${officer.department} department. Required department: ${training.allowedDepts.join(', ')}.`
    };
  }

  // 2. Grade Check
  if (training.reqGrade && !training.reqGrade.includes(officer.grade)) {
    return {
      eligible: false,
      message: 'Officer is not eligible for this training programme.',
      reason: `Officer has ${officer.grade}. Required grade: ${training.reqGrade.join(' or ')}.`
    };
  }

  // 3. Years of Service Check
  if (training.minYears > 0 && officer.yearsOfService < training.minYears) {
    return {
      eligible: false,
      message: 'Officer is not eligible for this training programme.',
      reason: `Officer has ${officer.yearsOfService} years of service. Minimum required: ${training.minYears} years.`
    };
  }

  // 4. Previous 12 Months Check
  if (officer.lastAttended && officer.lastAttended.trainingId === training.id && officer.lastAttended.monthsAgo < 12) {
    return {
      eligible: false,
      message: 'Officer is not eligible.',
      reason: 'Officer participated in this training within the previous 12 months.'
    };
  }

  return {
    eligible: true,
    message: 'Officer is eligible for this training programme.',
    reason: ''
  };
}

// Display Result
function showResult(isEligible, message, reason) {
  const resultCard = document.getElementById('resultCard');
  const resultBox = document.getElementById('resultBox');
  const messageEl = document.getElementById('resultMessage');
  const reasonEl = document.getElementById('resultReason');

  resultCard.style.display = 'block';

  if (isEligible) {
    resultBox.className = 'result-box success';
    messageEl.innerText = `✅ ${message}`;
    reasonEl.innerText = '';
  } else {
    resultBox.className = 'result-box danger';
    messageEl.innerText = `❌ ${message}`;
    reasonEl.innerText = reason ? `Reason: ${reason}` : '';
  }
}

function hideResult() {
  document.getElementById('resultCard').style.display = 'none';
}
