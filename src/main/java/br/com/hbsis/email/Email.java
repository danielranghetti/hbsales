package br.com.hbsis.email;

import br.com.hbsis.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class Email {

    @Autowired
    private JavaMailSender mailSender;




    public void enviarEmailDataRetirada(Pedido pedido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("O pedido: " + pedido.getCodPedido() + " foi aprovado ");
        message.setText(pedido.getFuncionario().getNome() + "\r\n"
                + "A data de retirado do seu pedido é " + pedido.getData().plusDays(10));
        message.setTo(pedido.getFuncionario().geteMail());
        message.setFrom("enviaemaild@gmail.com");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}







