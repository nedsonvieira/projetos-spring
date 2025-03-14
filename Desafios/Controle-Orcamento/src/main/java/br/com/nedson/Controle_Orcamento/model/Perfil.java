package br.com.nedson.Controle_Orcamento.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "perfis")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    public boolean isAdmin() {
        return this.nome.equals("ROLE_ADMIN");
    }

    public boolean isUser() {
        return this.nome.equals("ROLE_USER");
    }

    @Override
    public String getAuthority() {
        return nome;
    }
}