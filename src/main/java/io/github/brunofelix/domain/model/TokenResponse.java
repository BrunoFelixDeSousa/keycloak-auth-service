package io.github.brunofelix.domain.model;

/**
 * Representa a resposta de autenticação contendo os tokens e informações relacionadas.
 * Esta classe é usada para encapsular os dados retornados após uma tentativa de autenticação bem-sucedida.
 */
public record TokenResponse(
    String accessToken,
    String refreshToken,
    long expiresIn,
    String tokenType
) {}
