let currentPage = 0, currentSize = 12, totalPages = 0;

async function init() {
  updateNavForAuth();
  await loadCategories();
  const params = new URLSearchParams(location.search);
  if (params.get('categoryId')) document.getElementById('filterCategory').value = params.get('categoryId');
  if (params.get('keyword')) document.getElementById('searchKeyword').value = params.get('keyword');
  if (params.get('ai') === '1') {
    toggleAI();
    if (params.get('query')) {
      document.getElementById('aiSearchInput').value = params.get('query');
      runAISearch();
      return;
    }
  }
  loadItems();
}

async function loadCategories() {
  const cats = await API.get('/api/categories');
  const sel = document.getElementById('filterCategory');
  cats.forEach(c => sel.innerHTML += `<option value="${c.id}">${c.icon} ${c.name}</option>`);
}

async function loadItems(page=0) {
  currentPage = page;
  const q = buildQuery(page);
  const res = await API.get('/api/items/search?' + q);
  document.getElementById('resultCount').textContent = `${res.totalElements} item${res.totalElements!==1?'s':''} found`;
  document.getElementById('itemsGrid').innerHTML = res.content?.map(renderItemCard).join('') || '<div class="empty-state"><p>No items found. Try different filters.</p></div>';
  totalPages = res.totalPages;
  renderPagination();
}

function buildQuery(page=0) {
  const params = new URLSearchParams();
  const keyword = document.getElementById('searchKeyword').value.trim();
  if (keyword) params.set('keyword', keyword);
  const cat = document.getElementById('filterCategory').value;
  if (cat) params.set('categoryId', cat);
  const type = document.getElementById('filterType').value;
  if (type) params.set('listingType', type);
  const cond = document.getElementById('filterCondition').value;
  if (cond) params.set('condition', cond);
  const min = document.getElementById('filterMinPrice').value;
  if (min) params.set('minPrice', min);
  const max = document.getElementById('filterMaxPrice').value;
  if (max) params.set('maxPrice', max);
  const loc = document.getElementById('filterLocation').value;
  if (loc) params.set('location', loc);
  params.set('sortBy', document.getElementById('sortBy').value);
  params.set('page', page); params.set('size', currentSize);
  return params.toString();
}

function applyFilters() { loadItems(0); }
function clearFilters() {
  document.getElementById('filterCategory').value = '';
  document.getElementById('filterType').value = '';
  document.getElementById('filterCondition').value = '';
  document.getElementById('filterMinPrice').value = '';
  document.getElementById('filterMaxPrice').value = '';
  document.getElementById('filterLocation').value = '';
  document.getElementById('searchKeyword').value = '';
  loadItems(0);
}

function renderPagination() {
  const p = document.getElementById('pagination');
  if (totalPages <= 1) { p.innerHTML=''; return; }
  let html = '';
  if (currentPage > 0) html += `<button onclick="loadItems(${currentPage-1})" class="btn btn-ghost btn-sm">← Prev</button>`;
  html += `<span>Page ${currentPage+1} of ${totalPages}</span>`;
  if (currentPage < totalPages-1) html += `<button onclick="loadItems(${currentPage+1})" class="btn btn-ghost btn-sm">Next →</button>`;
  p.innerHTML = html;
}

let aiMode = false;
function toggleAI() {
  aiMode = !aiMode;
  document.getElementById('aiSearchPanel').classList.toggle('hidden', !aiMode);
  document.getElementById('aiToggleBtn').textContent = aiMode ? '🔍 Normal Search' : '🤖 AI Mode';
}

async function runAISearch() {
  const q = document.getElementById('aiSearchInput').value.trim();
  if (!q) return;
  const res = await API.get(`/api/ai/search?query=${encodeURIComponent(q)}`);
  document.getElementById('browseAiSuggestion').textContent = res.aiSuggestion || '';
  document.getElementById('resultCount').textContent = `${res.results?.length||0} AI-matched items`;
  document.getElementById('itemsGrid').innerHTML = res.results?.map(renderItemCard).join('') || '<div class="empty-state"><p>No AI results. Try rephrasing.</p></div>';
  document.getElementById('pagination').innerHTML = '';
}

// item detail URL param
const urlId = new URLSearchParams(location.search).get('id');
if (!urlId) init();
