package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioCadastroDTO;
import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioDetalharDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ValidarCadastroUsuarioExcepiton;
import br.com.nedson.Controle_Orcamento.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private JacksonTester<UsuarioCadastroDTO> jsonCadastrar;

    @Test
    @DisplayName("Deveria devolver 201 - Created - ao tentar cadastrar um usuário com dados válidos")
    public void cadastrarDadosValidos() throws Exception {
        var dto = new UsuarioCadastroDTO("Nome", "email@test.com", "123456");

        doNothing().when(usuarioService).cadastrar(any(UsuarioCadastroDTO.class));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Usuário cadastrado")));

        verify(usuarioService, times(1)).cadastrar(any(UsuarioCadastroDTO.class));
    }

    @Test
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar um usuário com dados nulos")
    public void cadastrarDadosNulos() throws Exception {
        var dto = new UsuarioCadastroDTO(null, "email@test.com", null);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "nome", "mensagem", "O nome é obrigatório!"),
                        Map.of("campo", "senha", "mensagem", "A senha é obrigatória!"))));
    }

    @Test
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar um usuário com email inválido")
    public void cadastrarEmailInvalido() throws Exception {
        var dto = new UsuarioCadastroDTO("Nome", "emailtest.com", "123456");

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email com formato inválido")));
    }

    @Test
    @DisplayName("Deveria devolver 409 - Conflict - ao tentar cadastrar um usuário com email já cadastrado")
    public void cadastrarUsuarioDuplicado() throws Exception {
        var dto = new UsuarioCadastroDTO("Nome", "email@test.com", "123456");

        doThrow(new ValidarCadastroUsuarioExcepiton("Já existe um usário cadastrado com este email!"))
                .when(usuarioService).cadastrar(any(UsuarioCadastroDTO.class));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Já existe um usário cadastrado")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}, value = "admin")
    public void listarAllSucesso() throws Exception {
        var perfis = List.of("ROLE_USER");

        var usuarios = Arrays.asList(
                new UsuarioDetalharDTO("Nome 1", "email1@test.com", perfis),
                new UsuarioDetalharDTO("Nome 2", "email2@test.com", perfis)
        );

        when(usuarioService.listarAll()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Nome 1"))
                .andExpect(jsonPath("$[1].nome").value("Nome 2"));

        verify(usuarioService, times(1)).listarAll();
    }

    @Test
    public void listarAllSemAutenticacao() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    public void listarAllUsuarioSemPermissao() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}, value = "admin")
    public void listarByIdEncontrado() throws Exception {
        var id = 1L;
        var perfis = List.of("ROLE_USER");
        var usuario = new UsuarioDetalharDTO("Nome", "email@test.com", perfis);

        when(usuarioService.listarById(id)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome"))
                .andExpect(jsonPath("$.email").value("email@test.com"));

        verify(usuarioService, times(1)).listarById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listarByIdNaoEncontrado() throws Exception {
        var id = 999L;

        when(usuarioService.listarById(id)).thenThrow(new EntityNotFoundException("Usuário não encontrado!"));

        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuário não encontrado")));

        verify(usuarioService, times(1)).listarById(id);
    }

    @Test
    public void listarByIdSemAutenticacao() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    public void listarByIdUsuarioSemPermissao() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}