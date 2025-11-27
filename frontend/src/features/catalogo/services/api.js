const BASE_URL = '/api'

async function safeJson(res) {
  try { return await res.json() } catch { return null }
}

// Token helpers (localStorage)
export function setToken(token) { localStorage.setItem('vass_token', token) }
export function getToken() { return localStorage.getItem('vass_token') }
export function clearToken() { localStorage.removeItem('vass_token') }

export function parseJwt(token) {
  try {
    const payload = token.split('.')[1]
    const json = atob(payload.replaceAll('-', '+').replaceAll('_', '/'))
    return JSON.parse(json)
  } catch {
    return null
  }
}

async function authFetch(path, opts = {}) {
  const headers = Object.assign({}, opts.headers || {})
  const token = getToken()
  if (token) headers['Authorization'] = `Bearer ${token}`
  const res = await fetch(`${BASE_URL}${path}`, Object.assign({}, opts, { headers }))
  // Handle auth errors globally: if unauthorized, clear token and notify UI to open login
  if (res.status === 401) {
    clearToken()
    try { window.dispatchEvent(new CustomEvent('vass:unauth')) } catch {}
    const err = await safeJson(res)
    throw new Error(err?.error || 'Unauthorized')
  }
  if (res.status === 403) {
    const err = await safeJson(res)
    throw new Error(err?.error || 'Forbidden')
  }
  return res
}

export async function login(username, password) {
  const res = await fetch(`/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
    body: JSON.stringify({ username, password })
  })
  if (!res.ok) {
    const err = await safeJson(res)
    throw new Error(err?.error || `HTTP ${res.status}`)
  }
  const json = await res.json()
  if (json?.token) setToken(json.token)
  return json.token
}

export async function register(username, password) {
  const res = await fetch(`/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
    body: JSON.stringify({ username, password })
  })
  if (!res.ok) {
    const err = await safeJson(res)
    throw new Error(err?.message || err?.error || `HTTP ${res.status}`)
  }
  const json = await res.json()
  return json.message || 'Usuário criado'
}

export async function listarCategorias(signal) {
  const res = await authFetch(`/categoria`, {
    method: 'GET',
    headers: { 'Accept': 'application/json' },
    signal
  })
  if (!res.ok) {
    const err = await safeJson(res)
    const msg = err?.error || err?.message || `HTTP ${res.status}`
    throw new Error(msg)
  }
  return res.json()
}

export async function criarProduto(dto) {
  const res = await authFetch(`/produto`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (res.status === 201) return res.json()
  if (res.status === 400) {
    const err = await safeJson(res)
    throw new Error(err?.error || 'Dados inválidos')
  }
  throw new Error(`HTTP ${res.status}`)
}

export async function listarProdutos() {
  const res = await authFetch(`/produto`, {
    method: 'GET',
    headers: { 'Accept': 'application/json' }
  })
  if (!res.ok) {
    const err = await safeJson(res)
    const msg = err?.error || err?.message || `HTTP ${res.status}`
    throw new Error(msg)
  }
  return res.json()
}

export { safeJson }
