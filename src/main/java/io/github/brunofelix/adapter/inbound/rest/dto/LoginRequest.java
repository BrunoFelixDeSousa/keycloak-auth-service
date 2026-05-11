package io.github.brunofelix.adapter.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username obrigatório") String username,
    @NotBlank(message = "Password obrigatório") String password) {}
