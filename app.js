// ==========================================================
// TASK 2: CONNECTED TO SPRING BOOT MYSQL / H2 DATABASE
// ==========================================================

const API_BASE = 'http://localhost:8080/api';
const PROGRAM_ID = 1;
const MAX_CAPACITY = 3;

let nominationsList = [];

// Initialize & load data directly from Database on page load
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

// 2. Submit Nomination to Database
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
  const confCount = nominationsList.filter(n => n.status === 'CONFIRMED').length;
  nominationsList.push({
    id: Date.now(),
    officerName: name,
    department: 'IT',
    nominatedAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    status: confCount < MAX_CAPACITY ? 'CONFIRMED' : 'WAITING'
  });

  input.value = '';
  render();
}

// 3. Cancel Seat in Database & Trigger Auto-Promotion
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
    const next = nominationsList.find(n => n.status === 'WAITING');
    if (next) next.status = 'CONFIRMED';
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

// 5. Reload from DB
function loadDemo() {
  fetchNominationsFromDB();
}

function resetAll() {
  nominationsList = [];
  render();
}

// 6. Render UI
function render() {
  const conf = nominationsList.filter(n => n.status === 'CONFIRMED');
  const wait = nominationsList.filter(n => n.status === 'WAITING' || n.status === 'WAITING_LIST');

  // Stats & Progress
  document.getElementById('confCount').innerText = conf.length;
  document.getElementById('waitCount').innerText = wait.length;
  document.getElementById('tabConfNum').innerText = conf.length;
  document.getElementById('tabWaitNum').innerText = wait.length;
  document.getElementById('capText').innerHTML = `<strong>${conf.length} / ${MAX_CAPACITY}</strong>`;
  
  const percent = Math.min(100, Math.round((conf.length / MAX_CAPACITY) * 100));
  document.getElementById('barFill').style.width = `${percent}%`;

  // Confirmed Table
  const confBody = document.getElementById('confList');
  if (conf.length === 0) {
    confBody.innerHTML = `<tr><td colspan="5" class="empty">No confirmed participants in database.</td></tr>`;
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

  // Waiting Table
  const waitBody = document.getElementById('waitList');
  if (wait.length === 0) {
    waitBody.innerHTML = `<tr><td colspan="4" class="empty">Waiting list is empty in database.</td></tr>`;
  } else {
    waitBody.innerHTML = wait.map((n, i) => `
      <tr>
        <td>#${i + 1}</td>
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
