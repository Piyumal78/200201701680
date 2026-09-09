// ==========================================================
// TASK 2: LIMITED TRAINING CAPACITY (STRICT FIFO ALLOCATION)
// ==========================================================

const API_BASE = 'http://localhost:8080/api';
const PROGRAM_ID = 1;
const MAX_CAPACITY = 3; // Standard Capacity for Demo

let nominationsList = [];

// Initialize & load data on startup
document.addEventListener('DOMContentLoaded', () => {
  fetchNominationsFromDB();
});

// 1. Fetch nominations from Database API
async function fetchNominationsFromDB() {
  try {
    const res = await fetch(`${API_BASE}/nominations/programme/${PROGRAM_ID}`);
    if (!res.ok) throw new Error('Backend HTTP ' + res.status);
    const data = await res.json();
    nominationsList = data;
    render();
  } catch (err) {
    console.warn('Backend not reachable, operating in local mode:', err.message);
    render();
  }
}

// 2. Submit Nomination
async function nominate(e) {
  e.preventDefault();
  const input = document.getElementById('officerName');
  const name = input.value.trim();
  if (!name) return;

  try {
    const res = await fetch(`${API_BASE}/nominations/submit`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        programId: PROGRAM_ID,
        officerName: name
      })
    });

    if (res.ok) {
      input.value = '';
      await fetchNominationsFromDB();
      return;
    }
  } catch (err) {
    console.warn('Backend error, fallback to local mode:', err);
  }

  // Local fallback
  nominationsList.push({
    id: Date.now(),
    officerName: name,
    department: 'IT',
    nominatedAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    status: 'ACTIVE'
  });

  input.value = '';
  render();
}

// 3. Cancel Seat & Auto-Promote
async function cancel(id) {
  try {
    const res = await fetch(`${API_BASE}/nominations/${id}/cancel`, {
      method: 'PUT'
    });

    if (res.ok) {
      await fetchNominationsFromDB();
      return;
    }
  } catch (err) {
    console.warn('Backend error, fallback to local cancel:', err);
  }

  // Local fallback
  const item = nominationsList.find(n => n.id === id);
  if (item) {
    item.status = 'CANCELLED';
    render();
  }
}

// 4. Tab Switching
function switchTab(tab) {
  document.getElementById('btnTab1').className = tab === 'conf' ? 'tab active' : 'tab';
  document.getElementById('btnTab2').className = tab === 'wait' ? 'tab active' : 'tab';
  document.getElementById('viewConf').style.display = tab === 'conf' ? 'block' : 'none';
  document.getElementById('viewWait').style.display = tab === 'wait' ? 'block' : 'none';
}

// 5. Demo & Reset
function loadDemo() {
  nominationsList = [
    { id: 101, officerName: 'A. Perera', department: 'IT', nominatedAt: '09:01 AM', status: 'ACTIVE' },
    { id: 102, officerName: 'B. Silva', department: 'FIN', nominatedAt: '09:03 AM', status: 'ACTIVE' },
    { id: 103, officerName: 'C. Fernando', department: 'HRM', nominatedAt: '09:05 AM', status: 'ACTIVE' },
    { id: 104, officerName: 'D. Jayawardena', department: 'OPS', nominatedAt: '09:08 AM', status: 'ACTIVE' },
    { id: 105, officerName: 'E. Watson', department: 'IT', nominatedAt: '09:12 AM', status: 'ACTIVE' }
  ];
  render();
}

function resetAll() {
  nominationsList = [];
  render();
}

// 6. STRICT FIFO RENDER ENGINE
function render() {
  // Filter only active nominations (exclude CANCELLED)
  const activeList = nominationsList.filter(n => n.status !== 'CANCELLED');

  // STRICT FIFO LOGIC:
  // First MAX_CAPACITY (3) are ALWAYS CONFIRMED
  // Everything from index 3 onwards is ALWAYS WAITING LIST
  const conf = activeList.slice(0, MAX_CAPACITY);
  const wait = activeList.slice(MAX_CAPACITY);

  // Update Counters
  document.getElementById('confCount').innerText = conf.length;
  document.getElementById('waitCount').innerText = wait.length;
  document.getElementById('tabConfNum').innerText = conf.length;
  document.getElementById('tabWaitNum').innerText = wait.length;
  document.getElementById('capText').innerHTML = `<strong>${conf.length} / ${MAX_CAPACITY}</strong>`;

  const percent = Math.min(100, Math.round((conf.length / MAX_CAPACITY) * 100));
  document.getElementById('barFill').style.width = `${percent}%`;

  // Render Confirmed Table (Max 3)
  const confBody = document.getElementById('confList');
  if (conf.length === 0) {
    confBody.innerHTML = `<tr><td colspan="5" class="empty">No confirmed participants yet.</td></tr>`;
  } else {
    confBody.innerHTML = conf.map((n, i) => `
      <tr>
        <td>#${i + 1}</td>
        <td><strong>${n.officerName || n.name}</strong> <small style="color:#64748b">(${n.department || 'IT'})</small></td>
        <td>${formatTime(n.nominatedAt || n.time)}</td>
        <td><span class="badge badge-conf">CONFIRMED</span></td>
        <td><button class="btn-cancel" onclick="cancel(${n.id})">Cancel</button></td>
      </tr>
    `).join('');
  }

  // Render Waiting Table (All 4th, 5th, ... 28th participants)
  const waitBody = document.getElementById('waitList');
  if (wait.length === 0) {
    waitBody.innerHTML = `<tr><td colspan="4" class="empty">Waiting list is empty.</td></tr>`;
  } else {
    waitBody.innerHTML = wait.map((n, i) => `
      <tr>
        <td>Queue #${i + 1}</td>
        <td><strong>${n.officerName || n.name}</strong> <small style="color:#64748b">(${n.department || 'OPS'})</small></td>
        <td>${formatTime(n.nominatedAt || n.time)}</td>
        <td><span class="badge badge-wait">WAITING</span></td>
      </tr>
    `).join('');
  }
}

function formatTime(val) {
  if (!val) return 'Just now';
  if (val.includes('T')) {
    return val.split('T')[1].substring(0, 5);
  }
  if (val.includes(' ')) {
    return val.split(' ')[1].substring(0, 5);
  }
  return val;
}
