package br.com.neves.vassourasecommerce.mapper;

import br.com.neves.vassourasecommerce.dto.ProdutoCreateRequest;
import br.com.neves.vassourasecommerce.dto.ProdutoResponse;
import br.com.neves.vassourasecommerce.models.Categoria;
import br.com.neves.vassourasecommerce.models.Produto;

public class ProdutoMapper {
    public static Produto toEntity(ProdutoCreateRequest req, Categoria categoria) {
        Produto p = new Produto();
        p.setNome(req.getNome());
        p.setPreco(req.getValor());
        p.setCategoria(categoria);
        return p;
    }
    public static ProdutoResponse toResponse(Produto p) {
        return new ProdutoResponse(p.getId(), p.getNome(), p.getPreco(), p.getCategoria().getId());
    }
}

