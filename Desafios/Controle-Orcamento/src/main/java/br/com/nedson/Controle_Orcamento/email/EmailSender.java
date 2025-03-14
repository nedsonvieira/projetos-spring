package br.com.nedson.Controle_Orcamento.email;

import br.com.nedson.Controle_Orcamento.infra.exception.EnviarEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$spring.mail.username")
    private String remetente;

    public String enviarEmail(String destinatario, String assunto, String mensagem){
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(remetente);
            email.setTo(destinatario);
            email.setSubject(assunto);
            email.setText(mensagem);

            javaMailSender.send(email);
            return "Email enviado!";

        } catch (EnviarEmailException e){
            throw new EnviarEmailException("Erro ao enviar email!");
        }
    }
}
