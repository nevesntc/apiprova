package br.com.neves.vassourasecommerce.repository;

import br.com.neves.vassourasecommerce.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

