package io.github.brunofelix.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TokenResponse")
class TokenResponseTest {

  @Nested
  @DisplayName("When creating TokenResponse")
  class WhenCreatingTokenResponse {

    @Test
    @DisplayName("Then should store access token correctly when valid data provided")
    void should_StoreAccessToken_When_ValidDataProvided() {
      // Given
      var accessToken = "eyJhbGciOiJSUzI1NiJ9.access";
      var refreshToken = "eyJhbGciOiJSUzI1NiJ9.refresh";
      var expiresIn = 300L;
      var tokenType = "Bearer";

      // When
      var response = new TokenResponse(accessToken, refreshToken, expiresIn, tokenType);

      // Then
      // assert response.accessToken().equals(accessToken);
      assertThat(response.accessToken()).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("Then should store refresh token correctly when valid data provided")
    void should_StoreRefreshToken_When_ValidDataProvided() {
      // Given
      var response = new TokenResponse("access", "refresh-token-value", 300L, "Bearer");

      // When & Then
      assertThat(response.refreshToken()).isEqualTo("refresh-token-value");
    }

    @Test
    @DisplayName("Then should store expires in correctly when valid data provided")
    void should_StoreExpiresIn_When_ValidDataProvided() {
      // Given
      var expiresIn = 300L;

      // When
      var response = new TokenResponse("access", "refresh", expiresIn, "Bearer");

      // Then
      assertThat(response.expiresIn()).isEqualTo(expiresIn);
    }

    @Test
    @DisplayName("Then should store token type correctly when valid data provided")
    void should_StoreTokenType_When_ValidDataProvided() {
      // Given
      var tokenType = "Bearer";

      // When
      var response = new TokenResponse("access", "refresh", 300L, tokenType);

      // Then
      assertThat(response.tokenType()).isEqualTo(tokenType);
    }
  }

  @Nested
  @DisplayName("When comparing TokenResponse")
  class WhenComparingTokenResponse {

    @Test
    @DisplayName("Then should be equal when same values provided")
    void should_BeEqual_When_SameValuesProvided() {
      // Given
      var response1 = new TokenResponse("access", "refresh", 300L, "Bearer");
      var response2 = new TokenResponse("access", "refresh", 300L, "Bearer");

      // When & Then
      assertThat(response1).isEqualTo(response2);
    }

    @Test
    @DisplayName("Then should not be equal when different access token provided")
    void should_NotBeEqual_When_DifferentAccessTokenProvided() {
      // Given
      var response1 = new TokenResponse("access-1", "refresh", 300L, "Bearer");
      var response2 = new TokenResponse("access-2", "refresh", 300L, "Bearer");

      // When & Then
      assertThat(response1).isNotEqualTo(response2);
    }
  }
}
