package br.com.neves.vassourasecommerce.service;

import br.com.neves.vassourasecommerce.dto.CategoriaCreateRequest;
import br.com.neves.vassourasecommerce.dto.CategoriaResponse;
import br.com.neves.vassourasecommerce.mapper.CategoriaMapper;
import br.com.neves.vassourasecommerce.models.Categoria;
import br.com.neves.vassourasecommerce.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CategoriaResponse criar(CategoriaCreateRequest req) {
        Categoria categoria = CategoriaMapper.toEntity(req);
        categoria = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(categoria);
    }
}

