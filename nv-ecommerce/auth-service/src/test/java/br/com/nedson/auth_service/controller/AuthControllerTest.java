package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.entity.RefreshToken;
import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.vo.Role;
import br.com.nedson.auth_service.domain.auth.vo.token.RefreshTokenRequest;
import br.com.nedson.auth_service.domain.auth.vo.token.TokenRequest;
import br.com.nedson.auth_service.infra.exception.TokenExpiradoException;
import br.com.nedson.auth_service.infra.exception.TokenInvalidoException;
import br.com.nedson.auth_service.infra.security.RefreshTokenService;
import br.com.nedson.auth_service.infra.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthControllerTest {

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<TokenRequest> jsonTokenRequest;

    @Autowired
    private JacksonTester<RefreshTokenRequest> jsonRefreshRequest;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarDadosDoTokenQuandoValido() throws Exception {
        var tokenValido = "token.mockado.jwt";
        var usuarioId = UUID.randomUUID();

        var usuarioMock = mock(Usuario.class);
        when(usuarioMock.getId()).thenReturn(usuarioId);
        when(usuarioMock.getEmail()).thenReturn("admin@teste.com");
        when(usuarioMock.getRole()).thenReturn(Role.ADMIN);

        when(tokenService.obterUsuario(tokenValido)).thenReturn(usuarioMock);
        when(tokenService.isExpirado(tokenValido)).thenReturn(false);

        var tokenRequest = new TokenRequest(tokenValido);

        mvc.perform(get("/nv-ecommerce/auth/introspect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenRequest.write(tokenRequest).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expirado").value(false))
                .andExpect(jsonPath("$.usuarioId").value(usuarioId.toString()))
                .andExpect(jsonPath("$.email").value("admin@teste.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void deveNegarAcessoParaUsuariosSemPermissao() throws Exception {
        var tokenRequest = new TokenRequest("qualquer.token");

        mvc.perform(get("/nv-ecommerce/auth/introspect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenRequest.write(tokenRequest).getJson()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Acesso negado")));
    }

    @Test
    void deveRenovarTokensComSucesso() throws Exception {
        var usuarioId = UUID.randomUUID();
        var usuarioMock = mock(Usuario.class);

        when(usuarioMock.getId()).thenReturn(usuarioId);
        when(usuarioMock.getEmail()).thenReturn("cliente@teste.com");
        when(usuarioMock.getRole()).thenReturn(Role.CLIENTE);

        var refreshToken = UUID.randomUUID().toString();
        var refreshTokenMock = mock(RefreshToken.class);
        var novoAccessToken = "access.token.mockado";
        var novoRefreshToken = "refresh.token.mockado";

        when(refreshTokenMock.getUsuario()).thenReturn(usuarioMock);
        when(refreshTokenService.validarRefreshToken(refreshToken)).thenReturn(refreshTokenMock);
        doNothing().when(refreshTokenService).revogarRefreshToken(usuarioId, refreshToken);
        when(tokenService.gerarToken(usuarioMock)).thenReturn(novoAccessToken);
        when(refreshTokenService.gerarRefreshToken(usuarioMock)).thenReturn(novoRefreshToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioMock, null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))
        );
        SecurityContextHolder.setContext(context);

        var refreshRequest = new RefreshTokenRequest(refreshToken);

        mvc.perform(post("/nv-ecommerce/auth/renovar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(refreshRequest).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(novoAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(novoRefreshToken))
                .andExpect(jsonPath("$.accessTokenExpiraEm").value(30 * 60))
                .andExpect(jsonPath("$.refreshTokenExpiraEm").value(7 * 24 * 60 * 60));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void renovar_deveRetornarErroQuandoRefreshTokenForInvalido() throws Exception {
        var refreshTokenInvalido = "refresh.token.invalido";

        when(refreshTokenService.validarRefreshToken(refreshTokenInvalido))
                .thenThrow(new TokenInvalidoException("Erro ao validar: Refresh token inválido ou não encontrado!"));

        var request = new RefreshTokenRequest(refreshTokenInvalido);

        mvc.perform(post("/nv-ecommerce/auth/renovar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao validar: Refresh token inválido ou não encontrado!")));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void renovar_deveRetornarErroQuandoRefreshTokenEstiverExpirado() throws Exception {
        var refreshToken = "refresh.token.expirado";

        when(refreshTokenService.validarRefreshToken(refreshToken))
                .thenThrow(new TokenExpiradoException("Refresh token expirado!"));

        var request = new RefreshTokenRequest(refreshToken);

        mvc.perform(post("/nv-ecommerce/auth/renovar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Refresh token expirado!")));
    }

    @Test
    void renovar_deveRetornarErroQuandoUsuarioNaoEstiverAutenticado() throws Exception {
        var request = new RefreshTokenRequest("qualquer.token");

        mvc.perform(post("/nv-ecommerce/auth/renovar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Falha na autenticação")));
    }

    @Test
    void revogar_deveRevogarRefreshTokenComSucesso() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioMock = mock(Usuario.class);

        when(usuarioMock.getId()).thenReturn(usuarioId);

        String refreshToken = UUID.randomUUID().toString();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        doNothing().when(refreshTokenService).revogarRefreshToken(usuarioId, refreshToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioMock, null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))
        );
        SecurityContextHolder.setContext(context);

        mvc.perform(delete("/nv-ecommerce/auth/revogar-token")
                        .principal(new UsernamePasswordAuthenticationToken(usuarioMock, null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Refresh token revogado com sucesso.")));
    }

    @Test
    void revogar_deveRetornarErroQuandoRefreshTokenForInvalido() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioMock = mock(Usuario.class);

        when(usuarioMock.getId()).thenReturn(usuarioId);

        String refreshToken = "token.invalido";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        doThrow(new TokenInvalidoException("Refresh token inválido!"))
                .when(refreshTokenService).revogarRefreshToken(usuarioId, refreshToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioMock, null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))
        );
        SecurityContextHolder.setContext(context);

        mvc.perform(delete("/nv-ecommerce/auth/revogar-token")
                        .principal(new UsernamePasswordAuthenticationToken(usuarioMock, null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Refresh token inválido!")));
    }

    @Test
    void revogar_deveRetornarErroQuandoUsuarioNaoEstiverAutenticado() throws Exception {
        String refreshToken = UUID.randomUUID().toString();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        mvc.perform(delete("/nv-ecommerce/auth/revogar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshRequest.write(request).getJson()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Falha na autenticação")));
    }

    @Test
    void deveRevogarTodosRefreshTokensComSucesso() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioMock = mock(Usuario.class);

        when(usuarioMock.getId()).thenReturn(usuarioId);
        when(refreshTokenService.revogarTodosRefreshToken(usuarioId)).thenReturn(3);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioMock, null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))
        );
        SecurityContextHolder.setContext(context);

        mvc.perform(delete("/nv-ecommerce/auth/revogar-todos")
                        .principal(new UsernamePasswordAuthenticationToken(usuarioMock, null)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total de refresh tokens revogados: 3")));
    }

    @Test
    void deveRetornarErroQuandoNenhumTokenForEncontrado() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuarioMock = mock(Usuario.class);

        when(usuarioMock.getId()).thenReturn(usuarioId);
        when(refreshTokenService.revogarTodosRefreshToken(usuarioId))
                .thenThrow(new EntityNotFoundException("Não existem refresh tokens para serem revogados!"));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioMock, null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))
        );
        SecurityContextHolder.setContext(context);

        mvc.perform(delete("/nv-ecommerce/auth/revogar-todos")
                        .principal(new UsernamePasswordAuthenticationToken(usuarioMock, null)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não existem refresh tokens para serem revogados!"));
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoAutenticado() throws Exception {
        mvc.perform(delete("/nv-ecommerce/auth/revogar-todos"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Falha na autenticação")));
    }
}