requireAuth();
const me = JSON.parse(localStorage.getItem('ss_user')||'{}');
if (me.role !== 'ADMIN') window.location = '/dashboard.html';
document.getElementById('adminUser').textContent = 'Admin: ' + me.name;

function showSection(name) {
  ['stats','users','items','reports'].forEach(s => {
    document.getElementById('section-'+s).classList.toggle('hidden', s!==name);
  });
  document.querySelectorAll('.sidebar-nav a').forEach(a => a.classList.remove('active'));
  event.target.classList.add('active');
  if (name==='users') loadUsers();
  if (name==='items') loadAdminItems();
  if (name==='reports') loadReports();
}

async function loadStats() {
  const s = await API.get('/api/admin/stats');
  document.getElementById('adminStats').innerHTML = [
    ['👥 Total Users', s.totalUsers], ['📦 Total Listings', s.totalItems],
    ['✅ Available Items', s.availableItems], ['🔄 Active Rentals', s.activeRentals],
    ['🎉 Completed Rentals', s.completedRentals], ['🛒 Total Orders', s.totalOrders],
    ['💰 Paid Orders', s.paidOrders], ['⚠️ Pending Reports', s.pendingReports],
    ['🚫 Blocked Users', s.blockedUsers]
  ].map(([label,val])=>`<div class="stat-card"><div class="stat-info"><span class="stat-val">${val}</span><span>${label}</span></div></div>`).join('');
}

async function loadUsers() {
  const users = await API.get('/api/admin/users');
  document.getElementById('adminUsersList').innerHTML = `<table class="admin-table">
    <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Rating</th><th>Actions</th></tr></thead>
    <tbody>${users.map(u=>`<tr>
      <td>${u.name}</td><td>${u.email}</td>
      <td><span class="badge-role">${u.role}</span></td>
      <td>${u.blocked?'<span class="badge-blocked">Blocked</span>':'<span class="badge-active">Active</span>'}</td>
      <td>${u.averageRating>0?'⭐'+u.averageRating.toFixed(1):'-'}</td>
      <td>${u.blocked
        ?`<button onclick="unblockUser(${u.id})" class="btn btn-success btn-sm">Unblock</button>`
        :`<button onclick="blockUser(${u.id})" class="btn btn-danger btn-sm">Block</button>`}
      </td></tr>`).join('')}</tbody></table>`;
}

async function loadAdminItems() {
  const items = await API.get('/api/admin/items');
  document.getElementById('adminItemsList').innerHTML = `<table class="admin-table">
    <thead><tr><th>Name</th><th>Owner</th><th>Category</th><th>Type</th><th>Status</th><th>Actions</th></tr></thead>
    <tbody>${items.map(i=>`<tr>
      <td>${i.name}</td><td>${i.owner?.name}</td>
      <td>${i.category?.name}</td><td>${i.listingType}</td><td>${i.status}</td>
      <td><button onclick="removeItem(${i.id})" class="btn btn-danger btn-sm">Remove</button></td>
    </tr>`).join('')}</tbody></table>`;
}

async function loadReports() {
  const reports = await API.get('/api/admin/reports');
  document.getElementById('adminReportsList').innerHTML = reports.length ?
    reports.map(r=>`<div class="rental-card">
      <div class="rental-info"><div>
        <strong>Reporter:</strong> ${r.reporter?.name}<br>
        ${r.reportedUser?`<strong>Reported User:</strong> ${r.reportedUser.name}<br>`:''}
        ${r.reportedItemName?`<strong>Reported Item:</strong> ${r.reportedItemName}<br>`:''}
        <strong>Reason:</strong> ${r.reason}<br>
        <span class="status-badge status-${r.status.toLowerCase()}">${r.status}</span>
      </div></div>
      ${r.status==='PENDING'?`<div class="rental-right">
        <button onclick="resolveReport(${r.id},'RESOLVED')" class="btn btn-success btn-sm">Resolve</button>
        <button onclick="resolveReport(${r.id},'DISMISSED')" class="btn btn-ghost btn-sm">Dismiss</button>
      </div>`:''}
    </div>`).join('') : '<p>No reports</p>';
}

async function blockUser(id) { await API.post(`/api/admin/users/${id}/block`); showToast('User blocked','info'); loadUsers(); }
async function unblockUser(id) { await API.post(`/api/admin/users/${id}/unblock`); showToast('User unblocked','success'); loadUsers(); }
async function removeItem(id) { if(!confirm('Remove item?'))return; await API.delete(`/api/admin/items/${id}`); showToast('Removed','success'); loadAdminItems(); }
async function resolveReport(id, status) { await API.post(`/api/admin/reports/${id}/resolve?status=${status}`); showToast('Report '+status.toLowerCase(),'success'); loadReports(); }

loadStats();
