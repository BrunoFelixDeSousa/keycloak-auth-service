package io.github.brunofelix.adapter.outbound.keycloak;

import io.github.brunofelix.domain.exception.AuthenticationException;
import io.github.brunofelix.domain.model.AuthCredentials;
import io.github.brunofelix.domain.model.TokenResponse;
import io.github.brunofelix.domain.port.out.AuthPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KeycloakAuthGateway implements AuthPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakAuthGateway.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SCOPE = "scope";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Inject
    @RestClient
    private KeycloakTokenClient tokenClient;

    @ConfigProperty(name = "keycloak.realm")
    private String realm;

    @ConfigProperty(name = "keycloak.client-id")
    private String clientId;

    @ConfigProperty(name = "keycloak.client-secret")
    private String clientSecret;

    @Override
    public TokenResponse login(AuthCredentials credentials) {
        LOGGER.debug("[KeycloakAuthGateway:login] Chamando token endpoint do Keycloak. realm={}", realm);

        var formParams = new MultivaluedHashMap<String, String>();
        formParams.add(GRANT_TYPE, "password");
        formParams.add(CLIENT_ID, clientId);
        formParams.add(CLIENT_SECRET, clientSecret);
        formParams.add(USERNAME, credentials.username());
        formParams.add(PASSWORD, credentials.password());
        formParams.add(SCOPE, "openid");

        return fetchToken(formParams, credentials.username());
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) throws AuthenticationException {
        LOGGER.debug("[KeycloakAuthGateway:refreshToken] Chamando refresh token no Keycloak. realm={}", realm);

        var formParams = new MultivaluedHashMap<String, String>();
        formParams.add(GRANT_TYPE, "refresh_token");
        formParams.add(CLIENT_ID, clientId);
        formParams.add(CLIENT_SECRET, clientSecret);
        formParams.add(REFRESH_TOKEN, refreshToken);

        return fetchToken(formParams, "refresh");
    }

    @Override
    public void logout(String refreshToken) throws AuthenticationException {
        LOGGER.debug("[KeycloakAuthGateway:logout] Revogando refresh token no Keycloak. realm={}", realm);

        var formParams = new MultivaluedHashMap<String, String>();
        formParams.add(CLIENT_ID, clientId);
        formParams.add(CLIENT_SECRET, clientSecret);
        formParams.add(REFRESH_TOKEN, refreshToken);

        try {
            tokenClient.logout(realm, formParams);
            LOGGER.info("[KeycloakAuthGateway:logout] Logout realizado com sucesso");
        } catch (WebApplicationException ex) {
            LOGGER.warn("[KeycloakAuthGateway:logout] Falha ao revogar token. Status: {}", ex.getResponse().getStatus());
            throw new AuthenticationException("Erro ao revogar token");

        }
    }

    private TokenResponse fetchToken(MultivaluedHashMap<String, String> form, String subject) {
        try {
            var kc = tokenClient.token(realm, form);
            return new TokenResponse(kc.accessToken(), kc.refreshToken(), kc.expiresIn(), kc.tokenType());
        } catch (WebApplicationException ex) {
            int status = ex.getResponse().getStatus();
            LOGGER.warn("[KeycloakAuthGateway:fetchToken] Falha na autenticao. subject={}, status={}", subject, status);
            throw new AuthenticationException("Credenciais invlidas ou token expirado");
        }
    }
}