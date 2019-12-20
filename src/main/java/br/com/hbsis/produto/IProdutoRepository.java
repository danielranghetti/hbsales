package br.com.hbsis.produto;


import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodProduto (String codProduto);
    Optional<Produto>  findByCodProduto(String codProduto);
    boolean existsById(Long id);
    Optional<Produto> findById(Long id);

}
