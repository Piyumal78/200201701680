// ==========================================================================
// SIMPLE TRAINING MANAGEMENT SYSTEM (TMS)
// ==========================================================================

let currentRole = 'ADMIN';

// Master Reference Data
const departments = [
  { id: 1, name: 'IT & Digital Transformation', code: 'IT' },
  { id: 2, name: 'Finance & Administration', code: 'FIN' },
  { id: 3, name: 'Human Resource Management', code: 'HR' },
  { id: 4, name: 'Operations & Planning', code: 'OPS' }
];

const venues = [
  { id: 1, name: 'Main Auditorium (Cap: 150)' },
  { id: 2, name: 'Executive Room A (Cap: 50)' },
  { id: 3, name: 'Digital Lab 102 (Cap: 30)' }
];

const trainers = [
  { id: 1, name: 'Dr. Aris Thorne (Internal)' },
  { id: 2, name: 'Prof. Elena Vance (External)' },
  { id: 3, name: 'Mr. Marcus Brody (Internal)' }
];

const officers = [
  { id: 101, empNo: 'EMP-101', name: 'A. Perera', email: 'perera@tms.gov', deptId: 1, designation: 'Senior Accountant' },
  { id: 102, empNo: 'EMP-102', name: 'David Miller', email: 'david@tms.gov', deptId: 1, designation: 'IT Lead' },
  { id: 103, empNo: 'EMP-103', name: 'Robert Chen', email: 'robert@tms.gov', deptId: 2, designation: 'Finance Officer' },
  { id: 104, empNo: 'EMP-104', name: 'Emily Watson', email: 'emily@tms.gov', deptId: 2, designation: 'Accountant' }
];

// Operational Data (Starts Empty - No Mock Records)
const programmes = [];
const nominations = [];

const DEFAULT_DEPT_QUOTA = 15;

document.addEventListener('DOMContentLoaded', () => {
  populateDropdowns();
  renderProgrammes();
  renderNominations();
  renderOfficers();
});

function populateDropdowns() {
  const venueSelect = document.getElementById('pVenue');
  const trainerSelect = document.getElementById('pTrainer');

  if (venueSelect) {
    venueSelect.innerHTML = '<option value="">Select Venue...</option>';
    venues.forEach(v => {
      venueSelect.innerHTML += `<option value="${v.id}">${v.name}</option>`;
    });
  }

  if (trainerSelect) {
    trainerSelect.innerHTML = '<option value="">Select Trainer...</option>';
    trainers.forEach(t => {
      trainerSelect.innerHTML += `<option value="${t.id}">${t.name}</option>`;
    });
  }
}

function showTab(tabId) {
  document.querySelectorAll('.tab-page').forEach(page => page.classList.remove('active'));
  document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));

  document.getElementById(tabId).classList.add('active');
  if (event && event.target) {
    event.target.classList.add('active');
  }
}

function changeRole() {
  currentRole = document.getElementById('roleSelect').value;
  renderProgrammes();
  renderNominations();
}

function toggleForm(formId) {
  const form = document.getElementById(formId);
  form.style.display = (form.style.display === 'none') ? 'block' : 'none';
}

// CONFLICT PREVENTION ENGINE
function checkConflict() {
  const startDate = document.getElementById('pStartDate').value;
  const endDate = document.getElementById('pEndDate').value;
  const startTime = document.getElementById('pStartTime').value;
  const endTime = document.getElementById('pEndTime').value;
  const venueId = parseInt(document.getElementById('pVenue').value);
  const trainerId = parseInt(document.getElementById('pTrainer').value);

  const alertBox = document.getElementById('conflictAlert');
  const saveBtn = document.getElementById('saveProgBtn');

  if (!startDate || !endDate || !venueId || !trainerId) {
    alertBox.style.display = 'none';
    saveBtn.disabled = false;
    return false;
  }

  let conflictFound = false;
  let errorMsg = '';

  for (let p of programmes) {
    if (p.status === 'CANCELLED') continue;

    const newStart = new Date(`${startDate}T${startTime}`);
    const newEnd = new Date(`${endDate}T${endTime}`);
    const existStart = new Date(`${p.startDate}T${p.startTime}`);
    const existEnd = new Date(`${p.endDate}T${p.endTime}`);

    const isOverlap = (newStart < existEnd) && (newEnd > existStart);

    if (isOverlap) {
      if (p.venueId === venueId) {
        conflictFound = true;
        errorMsg = `Venue Conflict: Selected venue is already booked for "${p.title}"!`;
        break;
      }
      if (p.trainerId === trainerId) {
        conflictFound = true;
        errorMsg = `Trainer Conflict: Selected trainer is already assigned to "${p.title}"!`;
        break;
      }
    }
  }

  if (conflictFound) {
    alertBox.innerHTML = `⚠️ <strong>${errorMsg}</strong>`;
    alertBox.style.display = 'block';
    saveBtn.disabled = true;
    return true;
  } else {
    alertBox.style.display = 'none';
    saveBtn.disabled = false;
    return false;
  }
}

function saveProgramme() {
  const title = document.getElementById('pTitle').value;
  const startDate = document.getElementById('pStartDate').value;
  const endDate = document.getElementById('pEndDate').value;
  const startTime = document.getElementById('pStartTime').value;
  const endTime = document.getElementById('pEndTime').value;
  const venueId = parseInt(document.getElementById('pVenue').value);
  const trainerId = parseInt(document.getElementById('pTrainer').value);
  const maxCapacity = parseInt(document.getElementById('pCapacity').value) || 40;

  if (!title || !startDate || !endDate || !venueId || !trainerId) {
    alert('Please fill all required fields!');
    return;
  }

  if (checkConflict()) return;

  const newProg = {
    id: 100 + programmes.length + 1,
    title: title,
    startDate: startDate,
    endDate: endDate,
    startTime: startTime,
    endTime: endTime,
    venueId: venueId,
    trainerId: trainerId,
    maxCapacity: maxCapacity,
    status: 'PUBLISHED'
  };

  programmes.push(newProg);
  toggleForm('createProgFormCard');
  renderProgrammes();
  alert('Programme created successfully!');
}

function renderProgrammes() {
  const tbody = document.getElementById('programmesTable');
  tbody.innerHTML = '';

  if (programmes.length === 0) {
    tbody.innerHTML = `<tr><td colspan="8" style="text-align:center; color:#888;">No training programmes created yet. Click "+ Add New Programme" above to create one.</td></tr>`;
    return;
  }

  programmes.forEach(p => {
    const venue = venues.find(v => v.id === p.venueId);
    const trainer = trainers.find(t => t.id === p.trainerId);
    const nomCount = nominations.filter(n => n.programId === p.id).length;

    let actionBtn = '';
    if (currentRole === 'ADMIN') {
      actionBtn = `<button class="btn btn-secondary btn-sm" onclick="cancelProgramme(${p.id})">Cancel</button>`;
    } else if (currentRole === 'DEPT_HEAD') {
      actionBtn = `<button class="btn btn-primary btn-sm" onclick="openNominationForm(${p.id})">+ Nominate</button>`;
    } else {
      actionBtn = `<span style="color:#666;">View Only</span>`;
    }

    tbody.innerHTML += `
      <tr>
        <td>#PROG-${p.id}</td>
        <td><strong>${p.title}</strong></td>
        <td>${p.startDate} (${p.startTime} - ${p.endTime})</td>
        <td>${venue ? venue.name : ''}</td>
        <td>${trainer ? trainer.name : ''}</td>
        <td>${nomCount} / ${p.maxCapacity}</td>
        <td><span class="badge badge-${p.status.toLowerCase()}">${p.status}</span></td>
        <td>${actionBtn}</td>
      </tr>
    `;
  });
}

function renderNominations() {
  const tbody = document.getElementById('nominationsTable');
  tbody.innerHTML = '';

  const summaryBox = document.getElementById('quotaSummaryBox');
  summaryBox.innerHTML = `<strong>Rule:</strong> Max <strong>${DEFAULT_DEPT_QUOTA} seats</strong> per department. Duplicate officer nominations per programme are strictly blocked.`;

  if (nominations.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:#888;">No nominations submitted yet. Switch role to "Department Head" to submit officer nominations.</td></tr>`;
    return;
  }

  nominations.forEach(n => {
    const p = programmes.find(prog => prog.id === n.programId);
    const o = officers.find(off => off.id === n.officerId);
    const d = departments.find(dept => dept.id === n.deptId);

    let actionBtn = '';
    if (currentRole === 'ADMIN' && n.status === 'SUBMITTED') {
      actionBtn = `<button class="btn btn-success btn-sm" onclick="approveNomination(${n.id})">Approve</button>`;
    } else if (currentRole === 'TRAINER' && n.status === 'APPROVED') {
      actionBtn = `<button class="btn btn-primary btn-sm" onclick="markAttended(${n.id})">Mark Attended</button>`;
    } else {
      actionBtn = `<span style="color:#888;">Done</span>`;
    }

    tbody.innerHTML += `
      <tr>
        <td>#NOM-${n.id}</td>
        <td>${p ? p.title : ''}</td>
        <td><strong>${o ? o.name : ''}</strong> (${o ? o.empNo : ''})</td>
        <td>${d ? d.code : ''}</td>
        <td><span class="badge badge-${n.status.toLowerCase()}">${n.status}</span></td>
        <td>${actionBtn}</td>
      </tr>
    `;
  });
}

function renderOfficers() {
  const tbody = document.getElementById('officersTable');
  const searchInput = document.getElementById('officerSearch');
  const search = searchInput ? searchInput.value.toLowerCase() : '';
  tbody.innerHTML = '';

  officers.forEach(o => {
    const d = departments.find(dept => dept.id === o.deptId);
    const attendedCount = nominations.filter(n => n.officerId === o.id && n.status === 'ATTENDED').length;

    if (search && !o.name.toLowerCase().includes(search) && !o.empNo.toLowerCase().includes(search)) {
      return;
    }

    tbody.innerHTML += `
      <tr>
        <td><code>${o.empNo}</code></td>
        <td><strong>${o.name}</strong></td>
        <td>${o.email}</td>
        <td>${d ? d.name : ''}</td>
        <td>${o.designation}</td>
        <td>${attendedCount} Completed</td>
      </tr>
    `;
  });
}

let selectedProgIdForNom = null;

function openNominationForm(programId) {
  selectedProgIdForNom = programId;
  const p = programmes.find(prog => prog.id === programId);
  document.getElementById('nomProgTitle').value = p ? p.title : '';

  const select = document.getElementById('nomOfficerSelect');
  select.innerHTML = '';

  officers.forEach(o => {
    select.innerHTML += `<option value="${o.id}">${o.name} (${o.empNo})</option>`;
  });

  document.getElementById('quotaAlert').style.display = 'none';
  toggleForm('nominationFormCard');
}

function submitNomination() {
  const officerId = parseInt(document.getElementById('nomOfficerSelect').value);
  if (!officerId) return;

  const alertBox = document.getElementById('quotaAlert');

  // 1. Duplicate Check
  const isDuplicate = nominations.some(n => n.programId === selectedProgIdForNom && n.officerId === officerId);
  if (isDuplicate) {
    alertBox.innerText = '❌ Officer is already nominated for this training programme.';
    alertBox.style.display = 'block';
    return;
  }

  // 2. Quota Check
  const userDeptId = 1;
  const currentDeptNoms = nominations.filter(n => n.programId === selectedProgIdForNom && n.deptId === userDeptId).length;

  if (currentDeptNoms >= DEFAULT_DEPT_QUOTA) {
    alertBox.innerText = `❌ Quota Limit Exceeded! Department cannot nominate more than ${DEFAULT_DEPT_QUOTA} officers.`;
    alertBox.style.display = 'block';
    return;
  }

  const selectedOfficer = officers.find(o => o.id === officerId);

  const newNom = {
    id: nominations.length + 1,
    programId: selectedProgIdForNom,
    officerId: officerId,
    deptId: selectedOfficer ? selectedOfficer.deptId : userDeptId,
    status: 'SUBMITTED'
  };

  nominations.push(newNom);
  toggleForm('nominationFormCard');
  renderNominations();
  alert('Nomination submitted successfully!');
}

function approveNomination(id) {
  const n = nominations.find(nom => nom.id === id);
  if (n) {
    n.status = 'APPROVED';
    renderNominations();
  }
}

function markAttended(id) {
  const n = nominations.find(nom => nom.id === id);
  if (n) {
    n.status = 'ATTENDED';
    renderNominations();
    renderOfficers();
  }
}

function cancelProgramme(id) {
  const p = programmes.find(prog => prog.id === id);
  if (p) {
    p.status = 'CANCELLED';
    renderProgrammes();
  }
}
