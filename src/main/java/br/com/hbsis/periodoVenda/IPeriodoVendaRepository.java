package br.com.hbsis.periodoVenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IPeriodoVendaRepository extends JpaRepository<PeriodoVenda, Long> {

    @Query(value = "select count(1) from seg_periodo_venda where data_final >= :dataInicio and id_fornecedor = :fornecedor", nativeQuery = true)
    long existDateAberta(
            @Param("dataInicio")
                    LocalDate dataInicio,
            @Param("fornecedor") long fornecedor
    );
}
