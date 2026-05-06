package io.github.brunofelix.adapter.outbound;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Cliente REST reativo para comunicação com o servidor Keycloak.
 *
 * <p>Encapsula as chamadas HTTP ao endpoint {@code /protocol/openid-connect} do Keycloak,
 * permitindo a obtenção de tokens de acesso e a revogação de sessões via refresh token.</p>
 *
 * <p>Configurado via {@code quarkus.rest-client.keycloak.url} no {@code application.properties}.</p>
 *
 * @see KeycloakAuthGateway
 */
@RegisterRestClient(configKey = "keycloak")
@Path("/realms/{realm}/protocol/openid-connect")
public interface KeycloakTokenClient {

    /**
     * Solicita um token de acesso ao Keycloak via {@code grant_type=password} ou
     * {@code grant_type=refresh_token}.
     *
     * <p>O formulário deve conter os campos obrigatórios para o tipo de grant desejado,
     * como {@code client_id}, {@code client_secret}, {@code username} e {@code password}
     * (para password flow) ou {@code refresh_token} (para refresh flow).</p>
     *
     * @param realm nome do realm configurado no Keycloak
     * @param form  mapa de parâmetros do formulário OAuth2
     * @return {@link KeycloakTokenResponse} contendo {@code access_token}, {@code refresh_token},
     *         {@code expires_in} e {@code token_type}
     * @throws jakarta.ws.rs.WebApplicationException se o Keycloak retornar erro HTTP (ex: 401 Unauthorized)
     */
    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    KeycloakTokenResponse token(
            @PathParam("realm") String realm,
            MultivaluedMap<String, String> form
    );

    /**
     * Revoga a sessão do usuário no Keycloak invalidando o refresh token fornecido.
     *
     * <p>Após o logout bem-sucedido, o refresh token não poderá mais ser utilizado
     * para obter novos tokens de acesso. Erros de revogação são tratados de forma
     * tolerante pelo {@link KeycloakAuthGateway}.</p>
     *
     * @param realm        nome do realm configurado no Keycloak
     * @param form         mapa de parâmetros do formulário contendo {@code client_id},
     *                     {@code client_secret} e {@code refresh_token}
     * @throws jakarta.ws.rs.WebApplicationException se o Keycloak retornar erro HTTP
     */
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void logout(
            @PathParam("realm") String realm,
            MultivaluedMap<String, String> form
    );
}