package br.com.neves.vassourasecommerce.controller;

import br.com.neves.vassourasecommerce.dto.CategoriaCreateRequest;
import br.com.neves.vassourasecommerce.dto.CategoriaResponse;
import br.com.neves.vassourasecommerce.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse criar(@RequestBody @Valid CategoriaCreateRequest req) {
        return categoriaService.criar(req);
    }
}
