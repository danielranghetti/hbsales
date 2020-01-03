package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoPedido {
    private final IPedidoRepository iPedidoRepository;

    @Autowired
    public ConexaoPedido(IPedidoRepository iPedidoRepository) {
        this.iPedidoRepository = iPedidoRepository;
    }

    public boolean existsByCodPedido(String codPedido) {
        return iPedidoRepository.existsByCodPedido(codPedido);
    }

    public List<Pedido> findByPeriodoVenda(PeriodoVenda periodoVenda) {
        List<Pedido> pedidoList = new ArrayList<>();
        try {
            pedidoList = iPedidoRepository.findByPeriodoVenda(periodoVenda);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidoList;
    }

    public List<Pedido> findByFuncionario(Funcionario funcionario) {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            pedidos = iPedidoRepository.findByFuncionario(funcionario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;

    }

    public Pedido findByPedidoId(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public  void deletePorId(Long id){
        this.iPedidoRepository.deleteById(id);
    }
    public Pedido save(Pedido pedido){
        try {
            pedido = iPedidoRepository.save(pedido);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedido;
    }


    public Optional<Pedido> findById(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);
        if (pedidoOptional.isPresent()){
            return pedidoOptional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe",id));
    }
}
