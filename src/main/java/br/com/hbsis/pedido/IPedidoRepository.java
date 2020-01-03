package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByPeriodoVenda(PeriodoVenda periodoVenda);
    List<Pedido> findByFuncionario(Funcionario funcionario);
    

    boolean existsByCodPedido(String codPedido);



}

