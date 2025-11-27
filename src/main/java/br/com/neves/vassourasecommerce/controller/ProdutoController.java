package br.com.neves.vassourasecommerce.controller;

import br.com.neves.vassourasecommerce.dto.ProdutoCreateRequest;
import br.com.neves.vassourasecommerce.dto.ProdutoResponse;
import br.com.neves.vassourasecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@RequestBody @Valid ProdutoCreateRequest req) {
        return produtoService.criar(req);
    }

    @GetMapping
    public java.util.List<ProdutoResponse> listar() {
        return produtoService.listar();
    }
}
