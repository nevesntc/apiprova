# VassCommerce Frontend

## Requisitos
- Node.js 18+

## Instalação
```bash
cd frontend
npm install
npm run dev
```
Acesse: http://localhost:5173

## Funcionalidades
- GET /api/categoria via listarCategorias()
- POST /api/produto via criarProduto()
- Tratamento de loading / erro / sucesso
- Cancelamento com AbortController no useEffect
- Formulário de criação com validação simples (campo vazio mostra erro do backend)
- Botão de retry em caso de erro

## Estrutura
```
frontend/
  src/
    services/api.js
    pages/CatalogoPage.jsx
    components/FormNovoProduto.jsx
```

## Ajustes
Se backend estiver em outra porta/host, ajuste proxy em vite.config.js.

## Próximos Passos (opcional)
- Paginação /categoria?page=1&size=10
- Skeleton loader
- Hook useFetchCategorias
- Testes unitários
```

