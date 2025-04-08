package br.com.nedson.auth_service.domain.auth.vo.token;

import br.com.nedson.auth_service.domain.auth.vo.Role;

import java.util.UUID;

public record TokenIntrospectResponse(
        boolean expirado,
        UUID usuarioId,
        String email,
        Role role
) {
}
