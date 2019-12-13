package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {
    @Override
    boolean existsById(Long id);
    boolean existsByCnpj (String cnpj);

    Optional<Fornecedor> findByCnpj(String cnpj);

}
