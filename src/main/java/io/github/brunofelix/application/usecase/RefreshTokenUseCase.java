package io.github.brunofelix.application.usecase;

import io.github.brunofelix.domain.model.TokenResponse;
import io.github.brunofelix.domain.port.out.AuthPort;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RefreshTokenUseCase {

  private final AuthPort authPort;

  private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenUseCase.class);

  public RefreshTokenUseCase(AuthPort authPort) {
    this.authPort = authPort;
  }

  public TokenResponse execute(String refreshToken) {
    LOGGER.info("[RefreshTokenUseCase:execute] Renovando access token via refresh token");
    return authPort.refreshToken(refreshToken);
  }
}
