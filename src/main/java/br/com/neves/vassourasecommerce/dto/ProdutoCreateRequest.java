package br.com.neves.vassourasecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProdutoCreateRequest {
    @NotBlank(message = "nome é obrigatório")
    @Size(min = 3, max = 100, message = "nome deve ter entre 3 e 100 caracteres")
    private String nome;
    @NotNull(message = "valor é obrigatório")
    private Double valor;
    @NotNull(message = "categoriaId é obrigatório")
    private Long categoriaId;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}

