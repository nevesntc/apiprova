package br.com.neves.vassourasecommerce.mapper;

import br.com.neves.vassourasecommerce.dto.CategoriaCreateRequest;
import br.com.neves.vassourasecommerce.dto.CategoriaResponse;
import br.com.neves.vassourasecommerce.models.Categoria;

public class CategoriaMapper {
    public static Categoria toEntity(CategoriaCreateRequest req) {
        Categoria c = new Categoria();
        c.setNome(req.getNome());
        return c;
    }
    public static CategoriaResponse toResponse(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome());
    }
}

