package io.github.brunofelix.application.usecase;

import io.github.brunofelix.domain.port.out.AuthPort;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LogoutUseCase {

  private final AuthPort authPort;

  private static final Logger LOGGER = LoggerFactory.getLogger(LogoutUseCase.class);

  public LogoutUseCase(AuthPort authPort) {
    this.authPort = authPort;
  }

  public void execute(String refreshToken) {
    LOGGER.info("[LogoutUseCase:execute] Iniciando logout");
    authPort.logout(refreshToken);
    LOGGER.info("[LogoutUseCase:execute] Logout concluído com sucesso");
  }
}
