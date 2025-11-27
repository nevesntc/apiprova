import React, { useState } from 'react'
import { login, register } from '@/features/catalogo/services/api'

export default function AuthModal({ open, onClose, onLogin }) {
  const [tab, setTab] = useState('login')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)

  if (!open) return null

  React.useEffect(()=>{
    function onKey(e){ if (e.key === 'Escape') onClose && onClose() }
    window.addEventListener('keydown', onKey)
    return ()=> window.removeEventListener('keydown', onKey)
  },[onClose])

  function reset() {
    setUsername('')
    setPassword('')
    setError(null)
    setSuccess(null)
  }

  function switchTab(newTab) {
    setTab(newTab)
    reset()
  }

  async function handleLogin(e) {
    e.preventDefault()
    try {
      setLoading(true); setError(null); setSuccess(null)
      const token = await login(username, password)
      onLogin && onLogin(token)
      onClose && onClose()
      reset()
    } catch (ex) {
      setError(ex.message)
    } finally { setLoading(false) }
  }

  async function handleRegister(e) {
    e.preventDefault()
    try {
      setLoading(true); setError(null); setSuccess(null)
      const msg = await register(username, password)
      setSuccess(msg)
      setTimeout(() => switchTab('login'), 1500)
    } catch (ex) {
      setError(ex.message)
    } finally { setLoading(false) }
  }

  return (
    <div className="auth-modal-overlay" onClick={onClose}>
      <div className="auth-modal" onClick={e=>e.stopPropagation()}>
        <div className="auth-tabs">
          <button className={`auth-tab ${tab==='login'?'active':''}`} onClick={()=>switchTab('login')}>Entrar</button>
          <button className={`auth-tab ${tab==='register'?'active':''}`} onClick={()=>switchTab('register')}>Cadastrar</button>
        </div>
        <div className="auth-content">
          {tab === 'login' ? (
            <form className="auth-form" onSubmit={handleLogin}>
              <div>
                <label>Usuário</label>
                <input type="text" value={username} onChange={e=>setUsername(e.target.value)} required />
              </div>
              <div>
                <label>Senha</label>
                <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required />
              </div>
              {error && <div className="auth-error">{error}</div>}
              <button className="btn-primary" type="submit" disabled={loading}>
                {loading ? 'Entrando...' : 'Entrar'}
              </button>
              <div style={{textAlign:'center',fontSize:13,color:'#6b7280'}}>
                Usuários padrão: <strong>admin</strong> / 123456 ou <strong>user</strong> / 654321
              </div>
            </form>
          ) : (
            <form className="auth-form" onSubmit={handleRegister}>
              <div>
                <label>Usuário</label>
                <input type="text" value={username} onChange={e=>setUsername(e.target.value)} required />
              </div>
              <div>
                <label>Senha</label>
                <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required minLength={6} />
              </div>
              {error && <div className="auth-error">{error}</div>}
              {success && <div className="auth-success">{success}</div>}
              <button className="btn-primary" type="submit" disabled={loading}>
                {loading ? 'Cadastrando...' : 'Cadastrar'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  )
}
