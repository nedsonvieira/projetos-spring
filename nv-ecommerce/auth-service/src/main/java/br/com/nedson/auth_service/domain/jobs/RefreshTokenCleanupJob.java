package br.com.nedson.auth_service.domain.jobs;

import br.com.nedson.auth_service.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCleanupJob {

    private final RefreshTokenRepository refreshTokenRepository;

    // Executa a cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void removerTokensExpirados() {
        int deletados = refreshTokenRepository.deleteAllByExpiracaoBefore(Instant.now());
        if (deletados > 0) {
            log.info("Tokens expirados removidos: {}", deletados);
        }
    }
}
