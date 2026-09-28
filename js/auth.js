if (document.getElementById('loginForm')) {
  document.getElementById('loginForm').addEventListener('submit', async e => {
    e.preventDefault();
    const btn = document.getElementById('loginBtn');
    btn.textContent = 'Signing in...'; btn.disabled = true;
    try {
      const res = await API.post('/api/auth/login', {
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
      });
      localStorage.setItem('ss_token', res.token);
      localStorage.setItem('ss_user', JSON.stringify(res));
      window.location = res.role === 'ADMIN' ? '/admin.html' : '/dashboard.html';
    } catch(err) {
      const el = document.getElementById('authError');
      el.textContent = err.message; el.classList.remove('hidden');
      btn.textContent = 'Sign In'; btn.disabled = false;
    }
  });
}

if (document.getElementById('registerForm')) {
  document.getElementById('registerForm').addEventListener('submit', async e => {
    e.preventDefault();
    try {
      const res = await API.post('/api/auth/register', {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        phone: document.getElementById('phone').value,
        college: document.getElementById('college').value,
        location: document.getElementById('location').value
      });
      localStorage.setItem('ss_token', res.token);
      localStorage.setItem('ss_user', JSON.stringify(res));
      window.location = '/dashboard.html';
    } catch(err) {
      const el = document.getElementById('authError');
      el.textContent = err.message; el.classList.remove('hidden');
    }
  });
}

function fillCreds(email, pass) {
  document.getElementById('email').value = email;
  document.getElementById('password').value = pass;
}
