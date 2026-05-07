package io.github.brunofelix.adapter.outbound.keycloak;

import io.github.brunofelix.domain.exception.AuthenticationException;
import io.github.brunofelix.domain.model.AuthCredentials;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("KeycloakAuthGateway")
public class KeycloakAuthGatewayTest {
    
    @Mock
    private KeycloakTokenClient tokenClient;

    @InjectMocks
    private KeycloakAuthGateway gateway;

    private static final String REALM = "keycloak-auth-service-helm";
    private static final String CLIENT_ID = "keycloak-auth-service";
    private static final String CLIENT_SECRET = "secret";

    @BeforeEach
    void beforeEach() {
        Mockito.clearInvocations(tokenClient);
        // Injetar valores de @ConfigProperty manualmente via reflection
        setField(gateway, "realm", REALM);
        setField(gateway, "clientId", CLIENT_ID);
        setField(gateway, "clientSecret", CLIENT_SECRET);
    }

    @Nested
    @DisplayName("When login is called")
    class WhenLoginIsCalled {

        private AuthCredentials credentials;
        private KeycloakTokenResponse keycloakResponse;

        @BeforeEach
        void setUp() {
            credentials = new AuthCredentials("felix.dev", "secret123");
            keycloakResponse = new KeycloakTokenResponse("access-token", "refresh-token", 300L, "Bearer");
            when(tokenClient.token(eq(REALM), any())).thenReturn(keycloakResponse);
        }

        @Test
        @DisplayName("Then should return access token when login succeeds")
        void should_ReturnAccessToken_When_LoginSucceeds() {
            // When
            var result = gateway.login(credentials);

            // Then
            assertThat(result.accessToken()).isEqualTo("access-token");
        }

        @Test
        @DisplayName("Then should return refresh token when login succeeds")
        void should_ReturnRefreshToken_When_LoginSucceeds() {
            // When
            var result = gateway.login(credentials);

            // Then
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
        }

        @Test
        @DisplayName("Then should call token client with correct realm when login requested")
        void should_CallTokenClient_When_LoginRequested() {
            // When
            gateway.login(credentials);

            // Then
            verify(tokenClient).token(eq(REALM), any(MultivaluedHashMap.class));
        }

        @Test
        @DisplayName("Then should throw AuthenticationException when keycloak returns 401")
        void should_ThrowAuthenticationException_When_KeycloakReturns401() {
            // Given
            Mockito.reset(tokenClient);
            var response = Response.status(401).build();
            when(tokenClient.token(eq(REALM), any()))
                    .thenThrow(new WebApplicationException(response));

            // When & Then
            assertThatThrownBy(() -> gateway.login(credentials))
                    .isInstanceOf(AuthenticationException.class)
                    .hasMessage("Credenciais inválidas ou token expirado");
        }
    }

    @Nested
    @DisplayName("When refresh is called")
    class WhenRefreshIsCalled {

        private KeycloakTokenResponse keycloakResponse;

        @BeforeEach
        void setUp() {
            keycloakResponse = new KeycloakTokenResponse("new-access", "new-refresh", 300L, "Bearer");
            when(tokenClient.token(eq(REALM), any())).thenReturn(keycloakResponse);
        }

        @Test
        @DisplayName("Then should return new access token when refresh succeeds")
        void should_ReturnNewAccessToken_When_RefreshSucceeds() {
            // When
            var result = gateway.refreshToken("valid-refresh-token");

            // Then
            assertThat(result.accessToken()).isEqualTo("new-access");
        }

        @Test
        @DisplayName("Then should throw AuthenticationException when refresh token is expired")
        void should_ThrowAuthenticationException_When_RefreshTokenExpired() {
            // Given
            Mockito.reset(tokenClient);
            var response = Response.status(401).build();
            when(tokenClient.token(eq(REALM), any()))
                    .thenThrow(new WebApplicationException(response));

            // When & Then
            assertThatThrownBy(() -> gateway.refreshToken("expired-token"))
                    .isInstanceOf(AuthenticationException.class)
                    .hasMessage("Credenciais inválidas ou token expirado");
        }
    }

    @Nested
    @DisplayName("When logout is called")
    class WhenLogoutIsCalled {

        @Test
        @DisplayName("Then should call logout on token client when refresh token provided")
        void should_CallLogout_When_RefreshTokenProvided() {
            // When
            gateway.logout("valid-refresh-token");

            // Then
            verify(tokenClient).logout(eq(REALM), any(MultivaluedHashMap.class));
        }

        @Test
        @DisplayName("Then should not throw exception when keycloak logout fails")
        void should_NotThrowException_When_KeycloakLogoutFails() {
            // Given
            var response = Response.status(400).build();
            Mockito.doThrow(new WebApplicationException(response))
                    .when(tokenClient).logout(eq(REALM), any());

            // When & Then ? não deve lançar exceção
            assertThat(gateway)
                    .satisfies(g -> g.logout("any-token"));
        }
    }

    // --- helpers ---

    private void setField(Object target, String fieldName, String value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao injetar campo: " + fieldName, e);
        }
    }
}
