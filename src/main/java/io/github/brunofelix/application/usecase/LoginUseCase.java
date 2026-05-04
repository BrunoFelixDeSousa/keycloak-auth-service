package io.github.brunofelix.application.usecase;

import io.github.brunofelix.domain.model.AuthCredentials;
import io.github.brunofelix.domain.model.TokenResponse;
import io.github.brunofelix.domain.port.out.AuthPort;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LoginUseCase {

    private final AuthPort authPort;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginUseCase.class);
    
    public LoginUseCase(AuthPort authPort) {
        this.authPort = authPort;
    }

    public TokenResponse execute(AuthCredentials credentials) {
        LOGGER.info("[LoginUseCase:execute] Iniciando login para usuário: {}", credentials.username());
        var token = authPort.login(credentials);
        LOGGER.info("[LoginUseCase:execute] Login concluído com sucesso para: {}", credentials.username());
        return token;
    }
}
