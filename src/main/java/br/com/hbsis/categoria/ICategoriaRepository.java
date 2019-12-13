package br.com.hbsis.categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeCategoria(String nomeCategoria);
    boolean existsCategoriaProdutoByFornecedorId(Long id);
    boolean existsByCodigoCategoria (String codigoCategoria);
    Optional<Categoria> findByCodigoCategoria(String codigoCategoria);


}
