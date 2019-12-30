package br.com.hbsis.ferramentas;


import br.com.hbsis.itens.Item;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class Email {
    @Autowired
    private final JavaMailSender mailSender;

    public Email(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void enviarEmailDataRetirada(Pedido pedido, List<Item> items) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("O pedido: " + pedido.getCodPedido() +" foi aprovado ");
        message.setText(pedido.getFuncionario().getNome()  + "\r\n"
                + "O seu pediodo: " + pedido.getCodPedido() + "\r\n" +
                "Do fornecedor: "+ pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "foi aprovado" + "\r\n" +
                 "Com os seguintes itens: " + "\r\n" +
                listaItens(items)+
                "A data de retirado do seu pedido é " + pedido.getPeriodoVenda().getDataRetirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\r\n" +
                "O local para a retirada é " + pedido.getPeriodoVenda().getFornecedor().getEndereco() + "\r\n" +"para mais informações entra em contato pelo Telefone: " +
                pedido.getPeriodoVenda().getFornecedor().getTelefone() + " ou pelo E-mail: " + pedido.getPeriodoVenda().getFornecedor().geteMail());
        message.setTo(pedido.getFuncionario().geteMail());
        message.setFrom("enviaemaild@gmail.com");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList listaItens(List<Item> itemList) {

        ArrayList<String> listagem = new ArrayList<>();

        for (Item item : itemList) {
            listagem.add("O produto: " + item.getProduto().getNome() + " Com a quantidade: " + item.getQuantidade() + "\r\n");
        }
        return listagem;
    }

}
