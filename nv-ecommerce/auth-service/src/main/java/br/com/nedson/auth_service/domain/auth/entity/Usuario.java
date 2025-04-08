package br.com.nedson.auth_service.domain.auth.entity;

import br.com.nedson.auth_service.domain.auth.vo.user.AtualizarUsuario;
import br.com.nedson.auth_service.domain.auth.vo.user.CadastrarUsuario;
import br.com.nedson.auth_service.domain.auth.vo.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Instant criadoEm;

    private Instant atualizadoEm;

    private Boolean ativo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    public Usuario(CadastrarUsuario dto) {
        this.nome = dto.nome();
        this.email = dto.email();

        this.criadoEm = Instant.now();
        this.role = Role.CLIENTE;
        this.ativo = true;
    }

    public void atualizar(AtualizarUsuario dto){
        if (dto.nome() != null && !dto.nome().isBlank()){
            this.nome = dto.nome();
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            this.senha = dto.senha();
        }
        this.atualizadoEm = Instant.now();
    }

    public void inativar(){
        this.ativo = false;
    }

    public void criptografarSenha(String senhaCriptografa){
        this.senha = senhaCriptografa;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
