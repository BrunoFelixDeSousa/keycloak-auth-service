package io.github.brunofelix.adapter.inbound.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.brunofelix.domain.model.TokenResponse;

public record TokenResponseDto(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("expires_in") long expiresIn,
    @JsonProperty("token_type") String tokenType) {
  public static TokenResponseDto from(TokenResponse domain) {
    return new TokenResponseDto(
        domain.accessToken(), domain.refreshToken(), domain.expiresIn(), domain.tokenType());
  }
}
