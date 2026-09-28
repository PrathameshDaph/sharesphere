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
    const res = await fetch(this.baseUrl + path, opts);
    if (res.status === 204) return null;
    const data = await res.json().catch(()=>({}));
    if (!res.ok) throw new Error(data.message || data.error || 'Request failed');
    return data;
  },

  get: (path) => API.request('GET', path),
  post: (path, body) => API.request('POST', path, body),
  put: (path, body) => API.request('PUT', path, body),
  patch: (path, body) => API.request('PATCH', path, body),
  delete: (path) => API.request('DELETE', path),
  uploadFile: (path, formData) => API.request('POST', path, formData, true),
};
