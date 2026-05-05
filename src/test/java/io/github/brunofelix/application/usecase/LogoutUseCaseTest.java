package io.github.brunofelix.application.usecase;

import io.github.brunofelix.domain.port.out.AuthPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;



@ExtendWith(MockitoExtension.class)
@DisplayName("LogoutUseCase")
class LogoutUseCaseTest {

    @Mock
    private AuthPort authPort;

    private LogoutUseCase logoutUseCase;

    @BeforeEach
    void beforeEach() {
        Mockito.clearInvocations(authPort);
        logoutUseCase = new LogoutUseCase(authPort);
    }

    @Nested
    @DisplayName("When valid refresh token provided")
    class WhenValidRefreshTokenProvided {

        private String refreshToken;

        @BeforeEach
        void setUp() {
            refreshToken = "valid-refresh-token";
        }

        @Test
        @DisplayName("Then should call authPort logout when refresh token provided")
        void should_CallAuthPortLogout_When_RefreshTokenProvided() {
            // When
            logoutUseCase.execute(refreshToken);

            // Then
            verify(authPort).logout(refreshToken);
        }
    }

    @Nested
    @DisplayName("When authPort logout fails silently")
    class WhenAuthPortLogoutFailsSilently {

        private String refreshToken;

        @BeforeEach
        void setUp() {
            refreshToken = "any-refresh-token";
            doThrow(new RuntimeException("Keycloak unavailable"))
                    .when(authPort).logout(refreshToken);
        }

        @Test
        @DisplayName("Then should propagate exception when authPort throws")
        void should_PropagateException_When_AuthPortThrows() {
            // When & Then
            org.assertj.core.api.Assertions
                    .assertThatThrownBy(() -> logoutUseCase.execute(refreshToken))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Keycloak unavailable");
        }
    }
}

