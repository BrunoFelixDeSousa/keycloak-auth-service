package io.github.brunofelix.domain.port.out;

import io.github.brunofelix.domain.exception.AuthenticationException;
import io.github.brunofelix.domain.model.AuthCredentials;
import io.github.brunofelix.domain.model.TokenResponse;

/**
 * Porta de saída para operações de autenticação.
 * Esta interface define o contrato para autenticar usuários e obter tokens de acesso.
 * Implementações desta interface podem se comunicar com serviços de autenticação externos, 
 * nesse caso o Keycloak, para validar as credenciais do usuário e retornar os tokens apropriados.
 */
public interface AuthPort {
    /**
     * Autentica um usuário com as credenciais fornecidas e retorna uma resposta de token
     *
     * @param credentials As credenciais de autenticação contendo nome de usuário e senha.
     * @return Uma TokenResponse contendo o token de acesso, token de atualização, tempo de expiração e tipo de token.
     * @throws AuthenticationException Se a autenticação falhar devido a credenciais inválidas ou outros problemas.
     */
    TokenResponse login(AuthCredentials credentials) throws AuthenticationException;
    /**
     * Refresca um token de acesso usando um token de atualização válido.
     *
     * @param refreshToken O token de atualização para obter um novo token de acesso.
     * @return Uma TokenResponse contendo o novo token de acesso, token de atualização, tempo de expiração e tipo de token.
     * @throws AuthenticationException Se a atualização do token falhar devido a um token de atualização inválido ou outros problemas.
     */
    TokenResponse refreshToken(String refreshToken) throws AuthenticationException;
    /**
     * Realiza o logout do usuário invalidando o token de atualização.
     *
     * @param refreshToken O token de atualização a ser invalidado para realizar o logout.
     * @throws AuthenticationException Se o logout falhar devido a um token de atualização inválido ou outros problemas.
     */
    void logout(String refreshToken) throws AuthenticationException;
    
}
