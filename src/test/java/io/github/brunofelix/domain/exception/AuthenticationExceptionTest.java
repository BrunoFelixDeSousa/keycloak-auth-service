package io.github.brunofelix.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthenticationException")
class AuthenticationExceptionTest {

  @Nested
  @DisplayName("When creating AuthenticationException")
  class WhenCreatingAuthenticationException {

    @Test
    @DisplayName("Then should store message correctly when message provided")
    void should_StoreMessage_When_MessageProvided() {
      // Given
      var message = "Credenciais inválidas ou token expirado";

      // When
      var exception = new AuthenticationException(message);

      // Then
      assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Then should be instance of RuntimeException when created")
    void should_BeInstanceOfRuntimeException_When_Created() {
      // When
      var exception = new AuthenticationException("erro");

      // Then
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Then should be throwable when thrown")
    void should_BeThrowable_When_Thrown() {
      // Given
      var message = "Credenciais inválidas ou token expirado";

      // When & Then
      assertThatThrownBy(
              () -> {
                throw new AuthenticationException(message);
              })
          .isInstanceOf(AuthenticationException.class)
          .hasMessage(message);
    }
  }
}
