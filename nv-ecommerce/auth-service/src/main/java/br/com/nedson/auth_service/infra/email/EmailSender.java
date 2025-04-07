package br.com.nedson.auth_service.infra.email;

import br.com.nedson.auth_service.infra.exception.EnviarEmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final EmailTemplate emailTemplate;

    public void enviar(String destinatario, String assunto, String template, String nomeUsuario){
        try {
            String conteudoDoEmail = emailTemplate.processarTemplate(template, nomeUsuario);

            // Cria a mensagem de e-mail
            MimeMessage email = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true); // "true" indica que o conteúdo é HTML

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(conteudoDoEmail, true);
            helper.setFrom("naoresponda@nv-ecommerce.com.br");

            javaMailSender.send(email);

        } catch (EnviarEmailException e){
            throw new EnviarEmailException("Erro ao enviar email!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
