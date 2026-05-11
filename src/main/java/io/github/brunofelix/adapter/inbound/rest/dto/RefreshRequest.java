package io.github.brunofelix.adapter.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
    @NotBlank(message = "Refresh token obrigatório") String refreshToken) {}
