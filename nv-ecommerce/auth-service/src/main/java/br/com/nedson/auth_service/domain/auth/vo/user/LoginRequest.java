package br.com.nedson.auth_service.domain.auth.vo.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Email é obrigatório para realizar autenticação!")
        @Email(message = "Formato de email inválido!")
        String email,

        @NotBlank(message = "Senha é obrigatória para realizar autenticação!")
        String senha
) {
}
