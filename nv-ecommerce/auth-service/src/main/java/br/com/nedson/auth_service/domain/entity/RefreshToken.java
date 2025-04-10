package br.com.nedson.auth_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(unique = true)
    private String token;

    private Instant expiracao;

    public RefreshToken(Usuario usuario) {
        this.token = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.expiracao = Instant.now().plusSeconds(60 * 60 * 24 * 7); // 7 dias
    }
}