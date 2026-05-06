package io.github.brunofelix.adapter.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear a resposta do Keycloak ao realizar login ou refresh token.
 */
public record KeycloakTokenResponse(
        @JsonProperty("access_token")  String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in")    long expiresIn,
        @JsonProperty("token_type")    String tokenType
) {}