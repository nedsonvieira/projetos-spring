package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.usuario.LoginDTO;
import br.com.nedson.Controle_Orcamento.infra.security.TokenService;
import br.com.nedson.Controle_Orcamento.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private JacksonTester<LoginDTO> jsonCadastrar;

    @Test
    public void efetuarComLoginSucesso() throws Exception {
        var dto = new LoginDTO("email@test.com", "123456");
        var tokenMock = "tokenTest";

        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        var authenticationMock = mock(Authentication.class);

        when(manager.authenticate(authenticationToken)).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(new Usuario());
        when(tokenService.gerarToken(any(Usuario.class))).thenReturn(tokenMock);

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().string(tokenMock));

        verify(manager, times(1)).authenticate(authenticationToken);
        verify(tokenService, times(1)).gerarToken(any(Usuario.class));
    }

    @Test
    public void efetuarLoginComCredenciaisInvalidas() throws Exception {
        var dto = new LoginDTO("email@test.com", "123456");

        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        when(manager.authenticate(authenticationToken)).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas"));
    }

    @Test
    public void efetuarLoginDadosInválidos() throws Exception {
        LoginDTO dto = new LoginDTO("", "");

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "email", "mensagem", "O email é obrigatório!"),
                        Map.of("campo", "senha", "mensagem", "A senha é obrigatória!"))));
    }
}