import React, { useState } from 'react'
import { criarProduto } from '@/features/catalogo/services/api'

export default function FormNovoProduto({ onCriado }) {
  const [nome, setNome] = useState('')
  const [valor, setValor] = useState('')
  const [categoriaId, setCategoriaId] = useState('')
  const [loading, setLoading] = useState(false)
  const [erro, setErro] = useState(null)

  async function onSubmit(e) {
    e.preventDefault()
    try {
      setLoading(true); setErro(null)
      await criarProduto({ nome, valor: Number(valor), categoriaId: Number(categoriaId) })
      setNome(''); setValor(''); setCategoriaId('')
      onCriado && onCriado()
    } catch (ex) {
      setErro(ex.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={onSubmit} style={{marginTop:8}}>
      <div className="form-row">
        <input placeholder="Nome" value={nome} onChange={e=>setNome(e.target.value)} />
      </div>
      <div className="form-row">
        <input placeholder="Valor" value={valor} type="number" step="0.01" onChange={e=>setValor(e.target.value)} />
        <input placeholder="Categoria ID" value={categoriaId} type="number" onChange={e=>setCategoriaId(e.target.value)} />
      </div>
      <div style={{display:'flex',justifyContent:'flex-end',gap:8}}>
        <button className="btn" disabled={loading} type="submit">{loading ? 'Enviando...' : 'Criar'}</button>
      </div>
      {erro && <p className="feedback">Erro: {erro}</p>}
    </form>
  )
}
