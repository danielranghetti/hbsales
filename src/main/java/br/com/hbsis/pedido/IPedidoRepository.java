package br.com.hbsis.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {


    Optional<Pedido> findByCodPedido(String codPedido);

    boolean existsByCodPedido(String codPedido);
}

