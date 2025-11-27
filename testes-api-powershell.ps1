# Testes de API - VassCommerce (PowerShell)
# Execute cada bloco separadamente no PowerShell

# =============================================================================
# 1. CADASTRO DE NOVO USUÁRIO (POST /auth/register)
# =============================================================================

$registerBody = @{
    username = "novousuario"
    password = "senha123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/auth/register" `
    -Method Post `
    -ContentType "application/json" `
    -Body $registerBody

# Resposta esperada (201): {"message":"Usuário criado com sucesso"}

# =============================================================================
# 2. LOGIN - ADMIN (POST /auth/login)
# =============================================================================

$loginAdmin = @{
    username = "admin"
    password = "123456"
} | ConvertTo-Json

$tokenAdmin = (Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
    -Method Post `
    -ContentType "application/json" `
    -Body $loginAdmin).token

Write-Host "Token Admin: $tokenAdmin" -ForegroundColor Green

# =============================================================================
# 3. LOGIN - USER (POST /auth/login)
# =============================================================================

$loginUser = @{
    username = "user"
    password = "654321"
} | ConvertTo-Json

$tokenUser = (Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
    -Method Post `
    -ContentType "application/json" `
    -Body $loginUser).token

Write-Host "Token User: $tokenUser" -ForegroundColor Cyan

# =============================================================================
# 4. ACESSAR ROTA PROTEGIDA SEM TOKEN (401 Unauthorized)
# =============================================================================

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/produto" -Method Get
} catch {
    Write-Host "Erro 401: $_" -ForegroundColor Red
}

# Resposta esperada: 401 Unauthorized

# =============================================================================
# 5. LISTAR PRODUTOS COM TOKEN USER (200 OK)
# =============================================================================

$headers = @{
    Authorization = "Bearer $tokenUser"
}

$produtos = Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
    -Method Get `
    -Headers $headers

$produtos | ConvertTo-Json

# Resposta esperada: Array de produtos (200)

# =============================================================================
# 6. LISTAR CATEGORIAS COM TOKEN USER (200 OK)
# =============================================================================

$categorias = Invoke-RestMethod -Uri "http://localhost:8080/api/categoria" `
    -Method Get `
    -Headers $headers

$categorias | ConvertTo-Json

# Resposta esperada: Array de categorias (200)

# =============================================================================
# 7. CRIAR PRODUTO COM TOKEN USER (403 Forbidden)
# =============================================================================

$novoProduto = @{
    nome = "Produto Teste"
    valor = 99.90
    categoriaId = 1
} | ConvertTo-Json

$headersUser = @{
    Authorization = "Bearer $tokenUser"
}

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
        -Method Post `
        -ContentType "application/json" `
        -Headers $headersUser `
        -Body $novoProduto
} catch {
    Write-Host "Erro 403 (esperado - USER não pode criar produto): $_" -ForegroundColor Yellow
}

# Resposta esperada: 403 Forbidden

# =============================================================================
# 8. CRIAR PRODUTO COM TOKEN ADMIN (201 Created)
# =============================================================================

$headersAdmin = @{
    Authorization = "Bearer $tokenAdmin"
}

$produtoCriado = Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
    -Method Post `
    -ContentType "application/json" `
    -Headers $headersAdmin `
    -Body $novoProduto

Write-Host "Produto criado com sucesso!" -ForegroundColor Green
$produtoCriado | ConvertTo-Json

# Resposta esperada: Produto criado (201)

# =============================================================================
# 9. TOKEN INVÁLIDO (401 Unauthorized)
# =============================================================================

$headersInvalido = @{
    Authorization = "Bearer token_invalido_123"
}

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
        -Method Get `
        -Headers $headersInvalido
} catch {
    Write-Host "Erro 401 (esperado - token inválido): $_" -ForegroundColor Red
}

# =============================================================================
# 10. TESTE COMPLETO - FLUXO CADASTRO → LOGIN → CRIAR PRODUTO
# =============================================================================

Write-Host "`n=== TESTE COMPLETO ===" -ForegroundColor Magenta

# Cadastrar novo usuário
$novoUser = @{
    username = "teste_$(Get-Random)"
    password = "senha123"
} | ConvertTo-Json

try {
    $cadastro = Invoke-RestMethod -Uri "http://localhost:8080/auth/register" `
        -Method Post `
        -ContentType "application/json" `
        -Body $novoUser
    Write-Host "✓ Cadastro: $($cadastro.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Cadastro falhou: $_" -ForegroundColor Red
}

# Login com o novo usuário
$loginNovo = @{
    username = ($novoUser | ConvertFrom-Json).username
    password = "senha123"
} | ConvertTo-Json

try {
    $tokenNovo = (Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $loginNovo).token
    Write-Host "✓ Login: Token obtido" -ForegroundColor Green
} catch {
    Write-Host "✗ Login falhou: $_" -ForegroundColor Red
}

# Listar produtos (deve funcionar - USER tem permissão)
try {
    $headersNovo = @{ Authorization = "Bearer $tokenNovo" }
    $prods = Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
        -Method Get `
        -Headers $headersNovo
    Write-Host "✓ Listagem: $($prods.Count) produtos encontrados" -ForegroundColor Green
} catch {
    Write-Host "✗ Listagem falhou: $_" -ForegroundColor Red
}

# Tentar criar produto (deve falhar - USER não tem ROLE_ADMIN)
try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/produto" `
        -Method Post `
        -ContentType "application/json" `
        -Headers $headersNovo `
        -Body $novoProduto
    Write-Host "✗ Criação de produto: Não deveria ter funcionado!" -ForegroundColor Red
} catch {
    Write-Host "✓ Criação de produto: Bloqueado corretamente (403)" -ForegroundColor Green
}

Write-Host "`n=== FIM DOS TESTES ===" -ForegroundColor Magenta

# =============================================================================
# DECODIFICAR JWT (PowerShell)
# =============================================================================

function Decode-JWT {
    param($token)

    $parts = $token -split '\.'
    if ($parts.Count -ne 3) {
        Write-Host "Token inválido" -ForegroundColor Red
        return
    }

    $payload = $parts[1]
    # Add padding if needed
    while ($payload.Length % 4 -ne 0) {
        $payload += '='
    }

    $decoded = [System.Text.Encoding]::UTF8.GetString(
        [System.Convert]::FromBase64String($payload)
    )

    $decoded | ConvertFrom-Json | ConvertTo-Json
}

# Usar:
# Decode-JWT -token $tokenAdmin
# Decode-JWT -token $tokenUser

