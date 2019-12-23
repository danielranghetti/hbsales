package br.com.hbsis.ferramentas;


import br.com.hbsis.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

@Component
public class Email {
    @Autowired
    private final JavaMailSender mailSender;

    public Email(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void enviarEmailDataRetirada(Pedido pedido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("O pedido: " + pedido.getCodPedido() +" foi aprovado ");
        message.setText(pedido.getFuncionario().getNome()  + "\r\n"
                + "O seu pediodo: " + pedido.getCodPedido() + "\r\n" +
                "Do fornecedor: "+ pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "foi aprovado" + "\r\n" +
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

}
