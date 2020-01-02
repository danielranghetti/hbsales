package br.com.hbsis.linhaCategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {

    boolean existsByCodLinhaCategoria(String codLinhaCategoria);

    Optional<LinhaCategoria> findByCodLinhaCategoria(String codLinhaCategoria);
}
