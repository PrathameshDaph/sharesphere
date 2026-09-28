requireAuth();
const me = JSON.parse(localStorage.getItem('ss_user') || '{}');

async function loadDashboard() {
  // Greeting
  const user = await API.get('/api/users/me').catch(() => me);
  document.getElementById('dashGreeting').innerHTML = `
    <h1>Hello, ${user.name?.split(' ')[0] || 'Student'} 👋</h1>
    <p>Welcome back to ShareSphere</p>`;

  const firstLetter = (user.name||'U')[0].toUpperCase();
  document.getElementById('sidebarUser').innerHTML = `
    <div class="sidebar-avatar">${firstLetter}</div>
    <div><strong>${user.name}</strong><br><small>${user.email}</small></div>`;

  document.getElementById('userAvatar').textContent = firstLetter;

  // Stats
  const [listings, myRentals, incoming, orders] = await Promise.all([
    API.get('/api/items/my'),
    API.get('/api/rentals/my'),
    API.get('/api/rentals/incoming'),
    API.get('/api/orders/my')
  ]);
  document.getElementById('dsListings').textContent = listings.length;
  document.getElementById('dsRentals').textContent = myRentals.filter(r=>r.status==='ACTIVE').length;
  document.getElementById('dsRequests').textContent = incoming.filter(r=>r.status==='REQUESTED').length;
  document.getElementById('dsOrders').textContent = orders.length;

  // Notifications
  const unread = await API.get('/api/notifications/unread-count').catch(()=>({count:0}));
  document.getElementById('notifCount').textContent = unread.count || '';

  // AI Recommendations
  const recs = await API.get('/api/ai/recommendations').catch(()=>[]);
  document.getElementById('recommendGrid').innerHTML = recs.map(renderItemCard).join('') ||
    '<p style="color:#888">Browse items to get personalised recommendations</p>';

  // Incoming requests
  const pendingReqs = incoming.filter(r => r.status === 'REQUESTED');
  const inSection = document.getElementById('incomingSection');
  if (pendingReqs.length) {
    document.getElementById('incomingList').innerHTML = pendingReqs.map(r => `
      <div class="rental-card">
        <div class="rental-info">
          <img src="${r.item?.imageUrls?.[0]||'/images/placeholder.svg'}" class="rental-thumb" onerror="this.src='/images/placeholder.svg'">
          <div><h3>${r.item?.name}</h3><p>${r.renter?.name} · ${r.durationDays} days · ₹${r.totalPayable}</p></div>
        </div>
        <div class="rental-right">
          <button onclick="quickAccept(${r.id})" class="btn btn-primary btn-sm">✅ Accept</button>
          <button onclick="quickReject(${r.id})" class="btn btn-danger btn-sm">❌ Reject</button>
        </div>
      </div>`).join('');
  } else { inSection.style.display = 'none'; }
}

async function doAISearch() {
  const q = document.getElementById('aiQuery').value.trim();
  if (!q) return;
  const res = await API.get(`/api/ai/search?query=${encodeURIComponent(q)}`).catch(e=>{showToast(e.message,'error');return null;});
  if (!res) return;
  document.getElementById('aiResults').classList.remove('hidden');
  document.getElementById('aiResultsTitle').textContent = `AI Results for "${q}"`;
  document.getElementById('aiSuggestion').textContent = res.aiSuggestion || '';
  document.getElementById('aiItemsGrid').innerHTML = res.results?.map(renderItemCard).join('') || '<p>No items found</p>';
}

function quickSearch(q) { document.getElementById('aiQuery').value = q; doAISearch(); }
async function quickAccept(id) { await API.post(`/api/rentals/${id}/accept`); showToast('Accepted!','success'); loadDashboard(); }
async function quickReject(id) { await API.post(`/api/rentals/${id}/reject`); showToast('Rejected','info'); loadDashboard(); }

loadDashboard();
updateNavForAuth();
