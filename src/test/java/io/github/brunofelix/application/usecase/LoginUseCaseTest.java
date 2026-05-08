package io.github.brunofelix.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.brunofelix.domain.exception.AuthenticationException;
import io.github.brunofelix.domain.model.AuthCredentials;
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
@DisplayName("LoginUseCase")
class LoginUseCaseTest {

  @Mock private AuthPort authPort;

  private LoginUseCase loginUseCase;

  @BeforeEach
  void beforeEach() {
    Mockito.clearInvocations(authPort);
    loginUseCase = new LoginUseCase(authPort);
  }

  @Nested
  @DisplayName("When valid credentials are provided")
  class WhenValidCredentialsAreProvided {

    private AuthCredentials credentials;
    private TokenResponse expectedToken;

    @BeforeEach
    void setUp() {
      credentials = new AuthCredentials("felix.dev", "secret123");
      expectedToken = new TokenResponse("access-token", "refresh-token", 300L, "Bearer");
      when(authPort.login(credentials)).thenReturn(expectedToken);
    }

    @Test
    @DisplayName("Then should return token response when login succeeds")
    void should_ReturnTokenResponse_When_LoginSucceeds() {
      // when
      var result = loginUseCase.execute(credentials);

      // then
      assertThat(result).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("Then should call authPort login when credentials provided")
    void should_CallAuthPortLogin_When_CredentialsProvided() {
      // When
      loginUseCase.execute(credentials);

      // Then
      verify(authPort).login(credentials);
    }

    @Test
    @DisplayName("Then should return access token correctly when login succeeds")
    void should_ReturnAccessToken_When_LoginSucceeds() {
      // When
      var result = loginUseCase.execute(credentials);

      // Then
      assertThat(result.accessToken()).isEqualTo("access-token");
    }
  }

  @Nested
  @DisplayName("When invalid credentials provided")
  class WhenInvalidCredentialsProvided {

    private AuthCredentials invalidCredentials;

    @BeforeEach
    void setUp() {
      invalidCredentials = new AuthCredentials("felix.dev", "wrong-password");
      when(authPort.login(invalidCredentials))
          .thenThrow(new AuthenticationException("Credenciais inválidas ou token expirado"));
    }

    @Test
    @DisplayName("Then should throw AuthenticationException when login fails")
    void should_ThrowAuthenticationException_When_LoginFails() {
      // When & Then
      assertThatThrownBy(() -> loginUseCase.execute(invalidCredentials))
          .isInstanceOf(AuthenticationException.class)
          .hasMessage("Credenciais inválidas ou token expirado");
    }
  }
}
