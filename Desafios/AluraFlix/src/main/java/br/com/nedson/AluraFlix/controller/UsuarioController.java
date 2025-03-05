package br.com.nedson.AluraFlix.controller;

import br.com.nedson.AluraFlix.dto.usuario.DadosAtualizarUsuario;
import br.com.nedson.AluraFlix.dto.usuario.DadosBuscarUsuario;
import br.com.nedson.AluraFlix.dto.usuario.DadosCadastrarUsuario;
import br.com.nedson.AluraFlix.dto.usuario.DadosDetalharUsuario;
import br.com.nedson.AluraFlix.model.Usuario;
import br.com.nedson.AluraFlix.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid DadosCadastrarUsuario dados) {
        if (usuarioRepository.existsByLogin(dados.login())) {
            throw new DataIntegrityViolationException("Já existe um usuário com este login!");
        }

        var usuario = new Usuario(
                dados.login(),
                passwordEncoder.encode(dados.senha()),
                dados.role()
        );
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalharUsuario(usuario));
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> listar() {
        var usuarios = usuarioRepository.findAll();
        var dados = usuarios.stream()
                .map(DadosDetalharUsuario::new)
                .toList();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DadosBuscarUsuario> listarById(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
        return ResponseEntity.ok(new DadosBuscarUsuario(usuario));
    }

    @PutMapping
    @SecurityRequirement(name = "bearer-key")
    @Transactional
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DadosDetalharUsuario> atualizar(@RequestBody @Valid DadosAtualizarUsuario dados) {
       try {
           var usuario = usuarioRepository.getReferenceById(dados.id());

           usuario.atualizar(dados.login(), passwordEncoder.encode(dados.senha()), dados.role());

           return ResponseEntity.ok().body(new DadosDetalharUsuario(usuario));
       } catch (DataIntegrityViolationException e){
           throw new DataIntegrityViolationException("Já existe um usuário com este login!", e);
       }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
        usuarioRepository.delete(usuario);

        return ResponseEntity.noContent().build();
    }
}