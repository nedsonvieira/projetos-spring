package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioCadastroDTO;
import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioDetalharDTO;
import br.com.nedson.Controle_Orcamento.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto){
        usuarioService.cadastrar(dto);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UsuarioDetalharDTO>> listarAll(){
        return ResponseEntity.ok().body(usuarioService.listarAll());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UsuarioDetalharDTO> listarById(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.listarById(id));
    }
}
