import React, { useEffect, useState } from 'react'
import { listarCategorias, listarProdutos } from '@/features/catalogo/services/api'
import { parseJwt, clearToken, getToken } from '@/features/catalogo/services/api'
import FormNovoProduto from '@/features/catalogo/components/FormNovoProduto'
import AuthModal from '@/features/catalogo/components/LoginModal'

export default function CatalogoPage() {
  const [dados, setDados] = useState([])
  const [produtos, setProdutos] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState(null)
  const [retryFlag, setRetryFlag] = useState(0)
  const [loginOpen, setLoginOpen] = useState(false)
  const [tokenInfo, setTokenInfo] = useState(() => parseJwt(getToken()))

  useEffect(() => {
    function onUnauth(){ setLoginOpen(true) }
    window.addEventListener('vass:unauth', onUnauth)
    return ()=> window.removeEventListener('vass:unauth', onUnauth)
  }, [])

  useEffect(() => {
    const token = getToken()
    if (!token) {
      // don't call protected endpoints without a token; prompt login instead
      setLoading(false)
      setLoginOpen(true)
      return
    }
    const ac = new AbortController()
    setLoading(true); setErro(null)
    Promise.all([listarCategorias(ac.signal), listarProdutos()])
      .then(([cats, prods]) => { setDados(cats); setProdutos(prods) })
      .catch(e => { if (e.name !== 'AbortError') setErro(e.message) })
      .finally(() => setLoading(false))
    return () => ac.abort()
  }, [retryFlag])

  function handleLogin(token) {
    setTokenInfo(parseJwt(token))
    setRetryFlag(f => f + 1)
  }

  function handleLogout() {
    clearToken()
    setTokenInfo(null)
    setRetryFlag(f => f + 1)
  }

  function tentarNovamente() { setRetryFlag(f => f + 1) }

  if (loading) return <p className="small">Carregando...</p>
  if (erro) return <div className="card"><p style={{color:'crimson'}}>Erro: {erro}</p><button onClick={tentarNovamente} className="btn">Tentar novamente</button></div>

  return (
    <div className="main-container">
      <header className="header">
        <div className="brand">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><rect width="24" height="24" rx="6" fill="#0ea5e9"/><path d="M7 12h10M7 8h10M7 16h6" stroke="#fff" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
          <h1>VassCommerce</h1>
          <div className="small">Catálogo</div>
        </div>
        <div className="controls">
          {tokenInfo ? (
            <div style={{display:'flex',gap:12,alignItems:'center'}}>
              <div style={{textAlign:'right'}}>
                <div className="small">{tokenInfo.sub}</div>
                <div className="small" title={tokenInfo.roles}>{(tokenInfo.roles || '').split(',').join(', ')}</div>
                <div className="small">exp: {tokenInfo.exp ? new Date(tokenInfo.exp*1000).toLocaleString() : '-'}</div>
              </div>
              <button className="btn" onClick={handleLogout} style={{background:'#ef4444'}}>Sair</button>
            </div>
          ) : (
            <div style={{display:'flex',gap:8}}>
              <button className="btn" onClick={()=>setLoginOpen(true)}>Entrar</button>
            </div>
          )}
        </div>
       </header>

      <AuthModal open={loginOpen} onClose={()=>setLoginOpen(false)} onLogin={handleLogin} />

      <div className="content">
        <section>
          <div className="card">
            <h3 style={{marginTop:0}}>Categorias</h3>
            <div className="categories-grid">
              {dados.map(c => (
                <div className="category" key={c.id}>
                  <h4>{c.nome}</h4>
                  <p>{c.descricao || '—'}</p>
                </div>
              ))}
            </div>
          </div>

          <div style={{height:16}} />

          <div className="card">
            <h3 style={{marginTop:0}}>Produtos</h3>
            {produtos.length === 0 ? <p className="small">Nenhum produto</p> : (
              <div style={{display:'grid',gap:8}}>
                {produtos.map(p => (
                  <div key={p.id} className="category">
                    <strong>{p.nome}</strong>
                    <div className="small">R$ {p.valor?.toFixed(2)} • Categoria {p.categoriaId}</div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>

        <aside>
          <div className="card">
            <h3 style={{marginTop:0}}>Novo Produto</h3>
            <FormNovoProduto onCriado={() => { alert('Produto criado!'); setRetryFlag(f=>f+1) }} />
            <p className="small" style={{marginTop:8}}>Use um ID de categoria existente (ex.: 1).</p>
          </div>
        </aside>
      </div>
    </div>
  )
}
