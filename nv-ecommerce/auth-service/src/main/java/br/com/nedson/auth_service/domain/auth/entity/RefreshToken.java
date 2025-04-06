package br.com.nedson.auth_service.domain.auth.entity;

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
    private UUID id;

    @ManyToOne
    private Usuario usuario;

    @Column(unique = true)
    private String token;

    private Instant expiracao;
}