package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.service.UsuarioService;
import br.com.nedson.auth_service.domain.auth.vo.user.AtualizarUsuario;
import br.com.nedson.auth_service.domain.auth.vo.user.CadastrarUsuario;
import br.com.nedson.auth_service.domain.auth.vo.user.DadosUsuarioResponse;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nv-ecommerce/auth/")
@AllArgsConstructor
@Tag(name = "Usuário", description = "Gerenciamento de usuários no NV e-Commerce")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Endpoint público para cadastro de novos usuários no sistema.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados"),
            @ApiResponse(responseCode = "409", description = "Conflito - Email já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@Valid @RequestBody CadastrarUsuario dto) {
        usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Atualiza os dados do usuário autenticado. É necessário estar autenticado e ter o papel 'ROLE_USER'.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - Papel insuficiente"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PutMapping("/atualizar")
    @PreAuthorize("hasRole('ADMIN', 'GESTOR', 'CLIENTE')")
    public ResponseEntity<String> atualizar(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody AtualizarUsuario dto) {
        usuarioService.atualizar(usuario.getId(), dto);
        return ResponseEntity.ok("Dados atualizados com sucesso!");
    }

    @Operation(
            summary = "Buscar usuário por e-mail",
            description = "Recupera os dados detalhados de um usuário pelo e-mail. É necessário estar autenticado e ter o papel 'ROLE_ADMIN'.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - Papel insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @GetMapping("/buscar/{email}")
    @PreAuthorize("hasRole('ADMIN', 'GESTOR')")
    public ResponseEntity<DadosUsuarioResponse> listarById(@PathVariable String email) {
        return ResponseEntity.ok().body(new DadosUsuarioResponse(usuarioService.findByEmail(email)));
    }

    @Operation(
            summary = "Inativar usuário",
            description = "Inativa um usuário pelo e-mail. É necessário estar autenticado e ter o papel 'ROLE_ADMIN'.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - Papel insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/inativar/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inativar(@PathVariable String email) {
        usuarioService.inativar(email);
        return ResponseEntity.noContent().build();
    }
}