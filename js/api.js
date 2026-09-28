// ── ShareSphere API Client with Hybrid Backend + Offline/GitHub Pages Demo Mock ──
const MOCK_CATEGORIES = [
  { id: 1, name: "Electronics", icon: "💻", description: "Phones, laptops, cameras, gadgets" },
  { id: 2, name: "Books", icon: "📚", description: "Textbooks, novels, reference books" },
  { id: 3, name: "Calculators", icon: "🧮", description: "Scientific and graphing calculators" },
  { id: 4, name: "Engineering Tools", icon: "📐", description: "Drawing sets, instruments, tools" },
  { id: 5, name: "Lab Equipment", icon: "🔬", description: "Microscopes, beakers, lab instruments" },
  { id: 6, name: "Sports Equipment", icon: "⚽", description: "Balls, rackets, gym equipment" },
  { id: 7, name: "Furniture", icon: "🪑", description: "Tables, chairs, shelves, lamps" },
  { id: 8, name: "Cycles", icon: "🚲", description: "Bicycles and accessories" },
  { id: 9, name: "Bags", icon: "🎒", description: "Backpacks, laptop bags, luggage" },
  { id: 10, name: "Musical Instruments", icon: "🎸", description: "Guitars, keyboards, drums" },
  { id: 11, name: "Event Equipment", icon: "🎤", description: "Projectors, mics, displays" },
  { id: 12, name: "Hostel Items", icon: "🏠", description: "Kettles, buckets, fans, utensils" },
  { id: 13, name: "Study Materials", icon: "📝", description: "Notes, question papers, assignments" },
  { id: 14, name: "Other", icon: "📦", description: "Everything else" }
];

const MOCK_ITEMS = [
  {
    id: 1,
    name: "Casio FX-991CW Scientific Calculator",
    description: "Latest Casio FX-991CW, 552 functions, natural display. Perfect for semester exams.",
    category: { id: 3, name: "Calculators", icon: "🧮" },
    condition: "Like New",
    price: 900,
    rentalPricePerDay: 30,
    securityDeposit: 300,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel A",
    averageRating: 4.8,
    owner: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    imageUrls: ["https://images.unsplash.com/photo-1594980596870-8aa52a78d8cd?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 2,
    name: "DSLR Camera Canon 200D",
    description: "Canon 200D with 18-55mm kit lens, 24.1MP. Great for photography projects and events.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "Good",
    price: 28000,
    rentalPricePerDay: 300,
    securityDeposit: 2000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel A",
    averageRating: 5.0,
    owner: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    imageUrls: ["https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 3,
    name: "Arduino Uno Starter Kit",
    description: "Arduino Uno R3 with 37-sensor kit, breadboard, jumper wires. Ideal for IoT projects.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "New",
    price: 1500,
    rentalPricePerDay: 50,
    securityDeposit: 500,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Lab Block",
    averageRating: 4.7,
    owner: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    imageUrls: ["https://images.unsplash.com/photo-1553406830-ef2513450d76?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 4,
    name: "Engineering Drawing Kit",
    description: "Staedtler 551 drawing set with compass, mini-drafter, set squares. First year essential.",
    category: { id: 4, name: "Engineering Tools", icon: "📐" },
    condition: "Good",
    price: 800,
    rentalPricePerDay: 20,
    securityDeposit: 200,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Block C",
    averageRating: 4.6,
    owner: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    imageUrls: ["https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 5,
    name: "Mountain Bicycle - Hero Sprint",
    description: "Hero Sprint 21-speed MTB, well maintained. Perfect for campus commute.",
    category: { id: 8, name: "Cycles", icon: "🚲" },
    condition: "Good",
    price: 6000,
    rentalPricePerDay: 100,
    securityDeposit: 1000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Parking Lot B",
    averageRating: 4.9,
    owner: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    imageUrls: ["https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 6,
    name: "Data Structures & Algorithms - CLRS 4th Ed",
    description: "Introduction to Algorithms by CLRS. Excellent condition, no markings.",
    category: { id: 2, name: "Books", icon: "📚" },
    condition: "Like New",
    price: 1200,
    rentalPricePerDay: null,
    securityDeposit: null,
    listingType: "SELL",
    status: "AVAILABLE",
    location: "Library Block",
    averageRating: 5.0,
    owner: { id: 5, name: "Sneha Patel", email: "sneha@college.edu", college: "IIT Campus", location: "Library Block", averageRating: 5.0 },
    imageUrls: ["https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 7,
    name: "Raspberry Pi 4 Model B (4GB)",
    description: "Raspberry Pi 4B 4GB RAM, unused. Comes with 32GB SD card and cooling case.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "New",
    price: 5500,
    rentalPricePerDay: 150,
    securityDeposit: 1000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Lab Block",
    averageRating: 4.9,
    owner: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    imageUrls: ["https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 8,
    name: "Wireless Bluetooth Speaker - JBL Go 3",
    description: "JBL Go 3, waterproof, 5hr battery. Great for room study jams or outdoor trips.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "Like New",
    price: 2500,
    rentalPricePerDay: 70,
    securityDeposit: 500,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel B",
    averageRating: 4.8,
    owner: { id: 5, name: "Sneha Patel", email: "sneha@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 5.0 },
    imageUrls: ["https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&auto=format&fit=crop&q=80"]
  }
];

const API = {
  baseUrl: '',
  getToken: () => localStorage.getItem('ss_token'),

  async request(method, path, body=null, isForm=false) {
    const headers = {};
    const token = this.getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;
    if (!isForm) headers['Content-Type'] = 'application/json';
    const opts = { method, headers };
    if (body) opts.body = isForm ? body : JSON.stringify(body);

    try {
      const res = await fetch(this.baseUrl + path, opts);
      if (res.status === 204) return null;
      if (res.ok) {
        return await res.json().catch(()=>({}));
      }
      // If error status, throw or fallback
      const data = await res.json().catch(()=>({}));
      throw new Error(data.message || data.error || 'Request failed');
    } catch (err) {
      // Offline / GitHub Pages static demo fallback
      return this.handleMockFallback(method, path, body);
    }
  },

  handleMockFallback(method, path, body) {
    if (path === '/api/categories') return MOCK_CATEGORIES;
    if (path === '/api/items/popular' || path === '/api/items/latest') return MOCK_ITEMS;
    if (path.startsWith('/api/items/search')) {
      return {
        content: MOCK_ITEMS,
        totalPages: 1,
        totalElements: MOCK_ITEMS.length,
        size: 12,
        number: 0
      };
    }
    if (path.startsWith('/api/items/')) {
      const id = parseInt(path.split('/')[3]);
      const found = MOCK_ITEMS.find(i => i.id === id) || MOCK_ITEMS[0];
      return found;
    }
    if (path.startsWith('/api/ai/search')) {
      const q = new URLSearchParams(path.split('?')[1]||'').get('query') || '';
      return {
        aiSuggestion: `AI matched ${MOCK_ITEMS.length} items based on "${q}" across categories.`,
        results: MOCK_ITEMS
      };
    }
    if (path.startsWith('/api/ai/similar')) return MOCK_ITEMS.slice(0, 3);
    if (path === '/api/admin/stats') {
      return {
        totalUsers: 6,
        totalItems: 15,
        availableItems: 14,
        activeRentals: 3,
        completedRentals: 8,
        totalOrders: 5,
        paidOrders: 4,
        pendingReports: 0,
        blockedUsers: 0
      };
    }
    if (path === '/api/auth/login') {
      const email = body?.email || 'rahul@college.edu';
      const isAdmin = email.includes('admin');
      const mockUser = {
        token: 'demo-token-12345',
        userId: isAdmin ? 1 : 2,
        name: isAdmin ? 'Admin' : 'Rahul Sharma',
        email: email,
        role: isAdmin ? 'ADMIN' : 'STUDENT'
      };
      return mockUser;
    }
    if (path === '/api/auth/register') {
      return {
        token: 'demo-token-12345',
        userId: 99,
        name: body?.name || 'New Student',
        email: body?.email || 'student@college.edu',
        role: 'STUDENT'
      };
    }
    if (path === '/api/items/my' || path === '/api/favorites' || path === '/api/rentals/my' || path === '/api/rentals/incoming' || path === '/api/orders/my' || path === '/api/conversations' || path === '/api/notifications') {
      return [];
    }
    if (path === '/api/notifications/unread-count') return { count: 0 };
    return {};
  },

  get: (path) => API.request('GET', path),
  post: (path, body) => API.request('POST', path, body),
  put: (path, body) => API.request('PUT', path, body),
  patch: (path, body) => API.request('PATCH', path, body),
  delete: (path) => API.request('DELETE', path),
  uploadFile: (path, formData) => API.request('POST', path, formData, true),
};
