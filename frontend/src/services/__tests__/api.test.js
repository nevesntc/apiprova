import { listarCategorias } from '../api.js'

test('listarCategorias aborta corretamente', async () => {
  const ac = new AbortController()
  ac.abort()
  await expect(listarCategorias(ac.signal)).rejects.toThrow()
})


