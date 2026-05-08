package io.github.brunofelix.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("AuthCredentials")
class AuthCredentialsTest {

  @Nested
  @DisplayName("When creating AuthCredentials")
  class WhenCreatingAuthCredentials {

    @Test
    @DisplayName("Then should create successfully when valid data provided")
    void should_CreateSuccessfully_When_ValidDataProvided() {
      // Given
      var username = "felix.dev";
      var password = "secret123";

      // When
      var credentials = new AuthCredentials(username, password);

      // Then
      assertThat(credentials.username()).isEqualTo(username);
    }

    @Test
    @DisplayName("Then should store password correctly when valid data provided")
    void should_StorePasswordCorrectly_When_ValidDataProvided() {
      // Given
      var username = "felix.dev";
      var password = "secret123";

      // When
      var credentials = new AuthCredentials(username, password);

      // Then
      assertThat(credentials.password()).isEqualTo(password);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Then should throw IllegalArgumentException when blank username provided")
    void should_ThrowIllegalArgumentException_When_BlankUsernameProvided(String username) {
      // When & Then
      assertThatThrownBy(() -> new AuthCredentials(username, "secret123"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username obrigatório");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Then should throw IllegalArgumentException when blank password provided")
    void should_ThrowIllegalArgumentException_When_BlankPasswordProvided(String password) {
      // When & Then
      assertThatThrownBy(() -> new AuthCredentials("felix.dev", password))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Password obrigatório");
    }
  }

  @Nested
  @DisplayName("When comparing AuthCredentials")
  class WhenComparingAuthCredentials {

    @Test
    @DisplayName("Then should be equal when same username and password provided")
    void should_BeEqual_When_SameUsernameAndPasswordProvided() {
      // Given
      var credentials1 = new AuthCredentials("felix.dev", "secret123");
      var credentials2 = new AuthCredentials("felix.dev", "secret123");

      // When & Then
      assertThat(credentials1).isEqualTo(credentials2);
    }

    @Test
    @DisplayName("Then should not be equal when different username provided")
    void should_NotBeEqual_When_DifferentUsernameProvided() {
      // Given
      var credentials1 = new AuthCredentials("felix.dev", "secret123");
      var credentials2 = new AuthCredentials("other.user", "secret123");

      // When & Then
      assertThat(credentials1).isNotEqualTo(credentials2);
    }
  }
}
