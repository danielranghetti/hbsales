package br.com.hbsis.itens;

import br.com.hbsis.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByPedido(Pedido pedido);
}
