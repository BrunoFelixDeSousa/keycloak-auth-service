package io.github.brunofelix.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.brunofelix.domain.exception.AuthenticationException;
import io.github.brunofelix.domain.model.TokenResponse;
import io.github.brunofelix.domain.port.out.AuthPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenUseCase")
class RefreshTokenUseCaseTest {

  @Mock private AuthPort authPort;

  private RefreshTokenUseCase refreshTokenUseCase;

  @BeforeEach
  void beforeEach() {
    Mockito.clearInvocations(authPort);
    refreshTokenUseCase = new RefreshTokenUseCase(authPort);
  }

  @Nested
  @DisplayName("When valid refresh token provided")
  class WhenValidRefreshTokenProvided {

    private String refreshToken;
    private TokenResponse expectedToken;

    @BeforeEach
    void setUp() {
      refreshToken = "valid-refresh-token";
      expectedToken = new TokenResponse("new-access-token", "new-refresh-token", 300L, "Bearer");
      when(authPort.refreshToken(refreshToken)).thenReturn(expectedToken);
    }

    @Test
    @DisplayName("Then should return new token response when refresh succeeds")
    void should_ReturnNewTokenResponse_When_RefreshSucceeds() {
      // When
      var result = refreshTokenUseCase.execute(refreshToken);

      // Then
      assertThat(result).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("Then should call authPort refresh when refresh token provided")
    void should_CallAuthPortRefresh_When_RefreshTokenProvided() {
      // When
      refreshTokenUseCase.execute(refreshToken);

      // Then
      verify(authPort).refreshToken(refreshToken);
    }

    @Test
    @DisplayName("Then should return new access token when refresh succeeds")
    void should_ReturnNewAccessToken_When_RefreshSucceeds() {
      // When
      var result = refreshTokenUseCase.execute(refreshToken);

      // Then
      assertThat(result.accessToken()).isEqualTo("new-access-token");
    }
  }

  @Nested
  @DisplayName("When expired refresh token provided")
  class WhenExpiredRefreshTokenProvided {

    private String expiredToken;

    @BeforeEach
    void setUp() {
      expiredToken = "expired-refresh-token";
      when(authPort.refreshToken(expiredToken))
          .thenThrow(new AuthenticationException("Credenciais inv�lidas ou token expirado"));
    }

    @Test
    @DisplayName("Then should throw AuthenticationException when refresh token is expired")
    void should_ThrowAuthenticationException_When_RefreshTokenExpired() {
      // When & Then
      assertThatThrownBy(() -> refreshTokenUseCase.execute(expiredToken))
          .isInstanceOf(AuthenticationException.class)
          .hasMessage("Credenciais inv�lidas ou token expirado");
    }
  }
}
