// ── Auth Helpers ─────────────────────────────────────────────────────────────────
function isLoggedIn() { return !!localStorage.getItem('ss_token'); }
function getUser() { return JSON.parse(localStorage.getItem('ss_user') || 'null'); }
function logout() { localStorage.clear(); window.location = 'login.html'; }

function requireAuth() {
  if (!isLoggedIn()) { window.location = 'login.html'; }
}

function updateNavForAuth() {
  const actions = document.getElementById('navActions');
  if (!actions) return;
  const user = getUser();
  if (user) {
    actions.innerHTML = `
      <a href="dashboard.html" class="btn btn-ghost">Dashboard</a>
      <a href="add-item.html" class="btn btn-primary">+ List Item</a>
      <div style="display:flex;align-items:center;gap:8px;cursor:pointer" onclick="toggleUserMenu()">
        <div class="avatar">${user.name?.[0]||'U'}</div>
      </div>
      <div class="user-menu hidden" id="userMenu">
        <a href="dashboard.html">Dashboard</a>
        <a href="my-listings.html">My Listings</a>
        <a href="rentals.html">Rentals</a>
        <a href="orders.html">Orders</a>
        ${user.role==='ADMIN'?'<a href="admin.html">Admin Panel</a>':''}
        <a href="#" onclick="logout()">Logout</a>
      </div>`;
    const nl = document.getElementById('navLinks');
    if (nl) nl.innerHTML = `<a href="items.html">Browse</a><a href="messages.html">Messages</a><a href="notifications.html">🔔</a>`;
  } else {
    actions.innerHTML = `<a href="login.html" class="btn btn-ghost">Login</a><a href="register.html" class="btn btn-primary">Get Started</a>`;
  }
}

function toggleUserMenu() {
  const m = document.getElementById('userMenu');
  if (m) m.classList.toggle('hidden');
}
document.addEventListener('click', e => {
  const m = document.getElementById('userMenu');
  if (m && !m.contains(e.target) && !e.target.closest('.avatar')) m.classList.add('hidden');
});

// ── Toast ────────────────────────────────────────────────────────────────────────
function showToast(msg, type='info') {
  const t = document.getElementById('toast');
  if (!t) return;
  t.textContent = msg;
  t.className = `toast toast-${type}`;
  setTimeout(() => t.className = 'toast hidden', 3500);
}

// ── Item Card Renderer ────────────────────────────────────────────────────────────
function renderItemCard(item) {
  const img = item.imageUrls?.[0] || 'images/placeholder.svg';
  const price = formatPrice(item);
  const rating = item.averageRating > 0 ? `⭐ ${item.averageRating.toFixed(1)}` : '';
  const fav = item.favorited ? '❤️' : '🤍';
  const user = getUser();
  return `
    <div class="item-card" onclick="window.location='item-detail.html?id=${item.id}'">
      <div class="item-image-wrap">
        <img src="${img}" alt="${item.name}" loading="lazy" onerror="this.src='images/placeholder.svg'">
        <span class="item-badge badge-${(item.listingType||'').toLowerCase()}">${item.listingType||''}</span>
        ${user ? `<button class="fav-btn" onclick="event.stopPropagation();toggleFav(${item.id},this)">${fav}</button>` : ''}
      </div>
      <div class="item-body">
        <div class="item-category">${item.category?.icon||''} ${item.category?.name||''}</div>
        <h3>${item.name}</h3>
        <div class="item-price">${price}</div>
        <div class="item-footer">
          <span class="item-condition">${item.condition||''}</span>
          <span class="item-rating">${rating}</span>
          <span class="item-location">📍 ${item.location||'Campus'}</span>
        </div>
      </div>
    </div>`;
}

function formatPrice(item) {
  let parts = [];
  if (item.price) parts.push(`₹${item.price}`);
  if (item.rentalPricePerDay) parts.push(`₹${item.rentalPricePerDay}/day`);
  if (!parts.length) parts.push('Free');
  return parts.join(' · ');
}

async function toggleFav(itemId, btn) {
  const isFav = btn.textContent.trim() === '❤️';
  if (isFav) {
    await API.delete(`/api/favorites/${itemId}`);
    btn.textContent = '🤍';
  } else {
    await API.post(`/api/favorites/${itemId}`);
    btn.textContent = '❤️';
  }
}
