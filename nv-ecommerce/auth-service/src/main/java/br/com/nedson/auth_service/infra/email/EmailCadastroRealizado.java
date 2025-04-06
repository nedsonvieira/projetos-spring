package br.com.nedson.auth_service.infra.email;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailCadastroRealizado {

    private final EmailSender emailSender;

    @Async
    public void enviar(String nomeUsuario, String destinatario) {

        emailSender.enviar(
                destinatario,
                "Bem-vindo ao Nv e-Commerce!!",
                "email-boas-vindas",
                nomeUsuario
        );
    }
}
