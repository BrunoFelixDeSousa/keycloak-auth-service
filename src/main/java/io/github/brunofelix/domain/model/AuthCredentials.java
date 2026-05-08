package io.github.brunofelix.domain.model;

/**
 * Representa as credenciais de autenticação usadas para login. Esta classe é usada para encapsular
 * o nome de usuário e a senha fornecidos pelo usuário durante o processo de autenticação. Inclui
 * validação para garantir que os campos não sejam nulos ou em branco, garantindo que as credenciais
 * sejam válidas antes de serem usadas para autenticação.
 */
public record AuthCredentials(String username, String password) {
  public AuthCredentials {
    if (username == null || username.isBlank())
      throw new IllegalArgumentException("Username obrigatório");
    if (password == null || password.isBlank())
      throw new IllegalArgumentException("Password obrigatório");
  }
}
