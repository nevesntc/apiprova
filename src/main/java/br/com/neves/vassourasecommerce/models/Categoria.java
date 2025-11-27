package br.com.neves.vassourasecommerce.models;

import jakarta.persistence.*;

@Entity
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private String imagemSimboloUrl;
    public Categoria() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getImagemSimboloUrl() { return imagemSimboloUrl; }
    public void setImagemSimboloUrl(String imagemSimboloUrl) { this.imagemSimboloUrl = imagemSimboloUrl; }
}

