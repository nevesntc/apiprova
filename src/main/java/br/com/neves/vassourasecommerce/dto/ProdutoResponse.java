package br.com.neves.vassourasecommerce.dto;

public class ProdutoResponse {
    private Long id;
    private String nome;
    private Double valor;
    private Long categoriaId;
    public ProdutoResponse(Long id, String nome, Double valor, Long categoriaId) {
        this.id = id; this.nome = nome; this.valor = valor; this.categoriaId = categoriaId;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Double getValor() { return valor; }
    public Long getCategoriaId() { return categoriaId; }
}

