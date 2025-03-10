package br.com.nedson.dt_itau.model;

import java.time.OffsetDateTime;

public record Transacao(Double valor, OffsetDateTime dataHora) {
}
