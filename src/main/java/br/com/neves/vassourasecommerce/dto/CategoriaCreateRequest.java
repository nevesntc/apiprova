package br.com.neves.vassourasecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaCreateRequest {
    @NotBlank(message = "nome é obrigatório")
    @Size(min = 3, max = 100, message = "nome deve ter entre 3 e 100 caracteres")
    private String nome;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

