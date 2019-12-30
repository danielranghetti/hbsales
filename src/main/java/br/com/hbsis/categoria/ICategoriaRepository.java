package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeCategoria(String nomeCategoria);
    boolean existsCategoriaProdutoByFornecedorId(Long id);
    boolean existsByCodigoCategoria (String codigoCategoria);
    Optional<Categoria> findByCodigoCategoria(String codigoCategoria);
    List<Categoria>  findByFornecedor(Fornecedor fornecedor);


}
