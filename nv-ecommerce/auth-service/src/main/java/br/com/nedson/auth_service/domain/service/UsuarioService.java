package br.com.nedson.auth_service.domain.service;

import br.com.nedson.auth_service.domain.entity.Usuario;
import br.com.nedson.auth_service.domain.repository.UsuarioRepository;
import br.com.nedson.auth_service.domain.vo.user.AtualizarUsuario;
import br.com.nedson.auth_service.domain.vo.user.CadastrarUsuario;
import br.com.nedson.auth_service.infra.email.EmailCadastroRealizado;
import br.com.nedson.auth_service.infra.exception.EmailCadastradoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repositorio;

    private final PasswordEncoder encoder;

    private final EmailCadastroRealizado enviarEmail;

    @Transactional
    public void cadastrar(CadastrarUsuario dto) {
        if (!repositorio.existsByEmail(dto.email())){
            var senhaCriptografada = encoder.encode(dto.senha());

            Usuario usuario = new Usuario(dto);
            usuario.criptografarSenha(senhaCriptografada);

            repositorio.save(usuario);
            enviarEmail.enviar(usuario.getNome(), usuario.getEmail());
        } else {
            throw new EmailCadastradoException("Já existe um usuário cadastrado com este email!");
        }
    }

    @Transactional
    public void atualizar(UUID id, AtualizarUsuario dto) {
        Usuario usuario = repositorio.getReferenceById(id);
        usuario.atualizar(dto);
    }

    public Usuario findByEmail(String email) {
        return verificarEmail(email);
    }

    @Transactional
    public void inativar(String email) {
        Usuario usuario = verificarEmail(email);
        usuario.inativar();
    }

    private Usuario verificarEmail(String email){
        return repositorio.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o email informado."));
    }
}