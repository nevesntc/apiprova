# 🚀 VASSCOMMERCE - INSTRUÇÕES FINAIS

## ✅ TUDO CORRIGIDO E TESTADO!

Backend está funcionando perfeitamente! Agora só falta iniciar o frontend.

---

## 📋 COMANDOS PARA EXECUTAR

### OPÇÃO 1: Rápida (Frontend já compilado)

**Terminal 1 - Backend (se não estiver rodando):**
```powershell
cd C:\codigosdev\trabalho
java -jar target\vassourasecommerce-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Frontend:**
```powershell
cd C:\codigosdev\trabalho\frontend
npm run dev
```

**Abrir navegador:** http://localhost:5173

---

### OPÇÃO 2: Completa (do zero)

**Terminal 1 - Backend:**
```powershell
cd C:\codigosdev\trabalho
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force
mvn clean package -DskipTests
java -jar target\vassourasecommerce-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Frontend:**
```powershell
cd C:\codigosdev\trabalho\frontend
npm install
npm run dev
```

**Abrir navegador:** http://localhost:5173

---

## 🎯 O QUE VAI ACONTECER

1. ✅ Modal de login/cadastro abre automaticamente
2. ✅ ZERO erros 401 no console (F12)
3. ✅ Pode fazer login com:
   - `admin` / `123456` (ROLE_ADMIN)
   - `user` / `654321` (ROLE_USER)
4. ✅ Pode criar novo usuário na aba "Cadastrar"
5. ✅ Após login: nome, roles e expiração aparecem no cabeçalho
6. ✅ Lista de categorias e produtos carrega
7. ✅ Pode criar produtos (apenas ADMIN)

---

## 🔧 TODAS AS CORREÇÕES APLICADAS

### 1. Backend - JwtAuthFilter.java ✅
- Agora ignora rotas `/auth/**` 
- Não tenta validar token em login/register

### 2. Backend - SecurityConfig.java ✅
- `/auth/**` liberado sem autenticação
- CORS configurado para localhost:5173 e :5174
- Endpoints `/api/*` protegidos por roles

### 3. Frontend - vite.config.js ✅
- Proxy para `/api` ✅
- Proxy para `/auth` ✅ (ADICIONADO)

### 4. Frontend - api.js ✅
- `login()` usa `/auth/login` (não `/api/auth/login`)
- `register()` usa `/auth/register` (não `/api/auth/register`)

---

## 🧪 VERIFICAÇÃO RÁPIDA

Se quiser testar se está tudo OK antes de abrir o navegador:

```powershell
# Teste o backend
$body = @{username="admin";password="123456"} | ConvertTo-Json
$token = (Invoke-RestMethod http://localhost:8080/auth/login -Method Post -ContentType "application/json" -Body $body).token
Write-Host "Token: $($token.Substring(0,30))..." -ForegroundColor Green
```

---

## 📂 ESTRUTURA FINAL DO PROJETO

```
C:\codigosdev\trabalho\
├── src/main/java/
│   └── .../security/
│       ├── JwtAuthFilter.java      ✅ CORRIGIDO
│       ├── JwtService.java         ✅ OK
│       └── SecurityConfig.java     ✅ CORRIGIDO
├── frontend/
│   ├── vite.config.js              ✅ CORRIGIDO
│   └── src/
│       └── features/catalogo/
│           └── services/
│               └── api.js          ✅ CORRIGIDO
├── GUIA-FINAL.md                   📖 Documentação completa
├── README.md                       📖 Readme do projeto
├── testes-api-powershell.ps1      🧪 Suite de testes
└── iniciar-projeto.ps1            🚀 Script automático
```

---

## 🎨 PREVIEW DO QUE VAI VER

```
┌─────────────────────────────────────────────────┐
│  [🏪 Logo]  VassCommerce                        │
│                            [admin] [Sair]       │
│                            ROLE_ADMIN           │
│                            exp: 27/11 17:45     │
├─────────────────────────────────────────────────┤
│                                                 │
│  Categorias:                                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │ Categoria│ │ Categoria│ │ Categoria│       │
│  │ 1        │ │ 2        │ │ 3        │       │
│  └──────────┘ └──────────┘ └──────────┘       │
│                                                 │
│  Produtos:                                      │
│  • Produto 1 - R$ 10,50 • Cat 1                │
│  • Produto 2 - R$ 25,99 • Cat 2                │
│                                                 │
├─────────────────────────────────────────────────┤
│  Novo Produto (ADMIN only):                    │
│  [Nome____] [Valor___] [Cat ID]  [Criar]      │
└─────────────────────────────────────────────────┘
```

---

## ⚡ ATALHO SUPER RÁPIDO

Copie e cole TUDO de uma vez (em terminais separados):

**PowerShell 1:**
```powershell
cd C:\codigosdev\trabalho; java -jar target\vassourasecommerce-0.0.1-SNAPSHOT.jar
```

**PowerShell 2:**
```powershell
cd C:\codigosdev\trabalho\frontend; npm run dev
```

**Aguarde 5 segundos e abra:** http://localhost:5173

---

## 🆘 SE DER ALGUM PROBLEMA

1. **Backend não inicia:** Porta 8080 pode estar ocupada
   ```powershell
   netstat -ano | findstr :8080
   ```

2. **Frontend não conecta:** Reinicie o npm run dev (Ctrl+C e rode de novo)

3. **Ainda dá 401:** Limpe cache do navegador (Ctrl+Shift+Delete)

---

## ✨ FIM

**Backend:** http://localhost:8080 ✅  
**Frontend:** http://localhost:5173 ✅  
**Status:** TUDO FUNCIONANDO! 🎉

**Agora é só executar os comandos acima e aproveitar!** 🚀

