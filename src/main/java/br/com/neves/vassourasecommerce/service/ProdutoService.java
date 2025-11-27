package br.com.neves.vassourasecommerce.service;

import br.com.neves.vassourasecommerce.dto.ProdutoCreateRequest;
import br.com.neves.vassourasecommerce.dto.ProdutoResponse;
import br.com.neves.vassourasecommerce.mapper.ProdutoMapper;
import br.com.neves.vassourasecommerce.models.Categoria;
import br.com.neves.vassourasecommerce.models.Produto;
import br.com.neves.vassourasecommerce.repository.CategoriaRepository;
import br.com.neves.vassourasecommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProdutoResponse criar(ProdutoCreateRequest req) {
        Categoria categoria = categoriaRepository.findById(req.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        Produto produto = ProdutoMapper.toEntity(req, categoria);
        produto = produtoRepository.save(produto);
        return ProdutoMapper.toResponse(produto);
    }

    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream()
                .map(ProdutoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
