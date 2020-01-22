package br.com.hbsis.itens;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
interface IItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByPedido(Pedido pedido);


    boolean existsByProdutoAndPedido(Produto produto, Pedido pedido);

    Optional<Item> findByProdutoAndPedido(Produto produto, Pedido pedido);
}
