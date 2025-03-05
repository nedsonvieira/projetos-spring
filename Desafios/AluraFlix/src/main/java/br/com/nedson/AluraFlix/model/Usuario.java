package br.com.nedson.AluraFlix.model;

import br.com.nedson.AluraFlix.dto.usuario.DadosAtualizarUsuario;
import br.com.nedson.AluraFlix.dto.usuario.Role;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Usuario(String login, String senha, Role role) {
        this.login = login;
        this.senha = senha;
        this.role = role;
    }

    public void atualizar(String login, String senha, Role role){
        if (!(login == null || login.trim().isEmpty())){
            this.login = login;
        }
        if (!(senha == null || senha.trim().isEmpty())){
            this.senha = senha;
        }
        if (!(role == null || role.name().isBlank())){
            this.role = role;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
