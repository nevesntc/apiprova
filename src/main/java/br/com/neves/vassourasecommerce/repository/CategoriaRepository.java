package br.com.neves.vassourasecommerce.repository;

import br.com.neves.vassourasecommerce.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

