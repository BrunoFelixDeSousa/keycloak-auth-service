package io.github.brunofelix.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.brunofelix.domain.model.TokenResponse;
import io.github.brunofelix.domain.port.out.AuthPort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefreshTokenUseCase {

    private final AuthPort authPort;

    private final static Logger LOGGER = LoggerFactory.getLogger(RefreshTokenUseCase.class);

    public RefreshTokenUseCase(AuthPort authPort) {
        this.authPort = authPort;
    }

    public TokenResponse execute(String refreshToken) {
        LOGGER.info("[RefreshTokenUseCase:execute] Renovando access token via refresh token");
        return authPort.refreshToken(refreshToken);
    }
    
}
