package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioCadastroDTO;
import br.com.nedson.Controle_Orcamento.dto.usuario.UsuarioDetalharDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ValidarCadastroUsuarioExcepiton;
import br.com.nedson.Controle_Orcamento.model.Perfil;
import br.com.nedson.Controle_Orcamento.model.Usuario;
import br.com.nedson.Controle_Orcamento.repository.PerfilRepository;
import br.com.nedson.Controle_Orcamento.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilRepository perfilRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void cadastrar(UsuarioCadastroDTO dto) {
        var isCadastrado = usuarioRepository.existsByEmail(dto.email());
        var perfil = perfilRepository.findByNome("ROLE_USER");

        if (isCadastrado){
            throw new ValidarCadastroUsuarioExcepiton("Já existe um usário cadastrado com este email!");
        }

        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfil);

        var usuario = new Usuario(
                dto.nome(),
                dto.email(),
                passwordEncoder.encode(dto.senha()),
                perfis);
        usuarioRepository.save(usuario);
    }

    public List<UsuarioDetalharDTO> listarAll(){
        var usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(UsuarioDetalharDTO::new)
                .toList();
    }

    public UsuarioDetalharDTO listarById(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        return new UsuarioDetalharDTO(usuario);
    }
}