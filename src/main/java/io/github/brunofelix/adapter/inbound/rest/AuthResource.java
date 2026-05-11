package io.github.brunofelix.adapter.inbound.rest;

import io.github.brunofelix.adapter.inbound.rest.dto.LoginRequest;
import io.github.brunofelix.adapter.inbound.rest.dto.RefreshRequest;
import io.github.brunofelix.adapter.inbound.rest.dto.TokenResponseDto;
import io.github.brunofelix.application.usecase.LoginUseCase;
import io.github.brunofelix.application.usecase.LogoutUseCase;
import io.github.brunofelix.application.usecase.RefreshTokenUseCase;
import io.github.brunofelix.domain.model.AuthCredentials;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

  private final LoginUseCase loginUseCase;
  private final LogoutUseCase logoutUseCase;
  private final RefreshTokenUseCase refreshTokenUseCase;

  public AuthResource(
      LoginUseCase loginUseCase,
      LogoutUseCase logoutUseCase,
      RefreshTokenUseCase refreshTokenUseCase) {
    this.loginUseCase = loginUseCase;
    this.logoutUseCase = logoutUseCase;
    this.refreshTokenUseCase = refreshTokenUseCase;
  }

  @POST
  @Path("/login")
  public Response login(@Valid LoginRequest request) {
    LOGGER.info(
        "[AuthResource:login] Requisição de login recebida. username={}", request.username());
    var credentials = new AuthCredentials(request.username(), request.password());
    var token = loginUseCase.execute(credentials);
    return Response.ok(TokenResponseDto.from(token)).build();
  }

  @POST
  @Path("/refresh")
  public Response refresh(@Valid RefreshRequest request) {
    LOGGER.info(
        "[AuthResource:refresh] Requisição de refresh recebida. refreshToken={}",
        request.refreshToken());
    var token = refreshTokenUseCase.execute(request.refreshToken());
    return Response.ok(TokenResponseDto.from(token)).build();
  }

  @POST
  @Path("/logout")
  public Response logout(@Valid RefreshRequest request) {
    LOGGER.info(
        "[AuthResource:logout] Requisição de logout recebida. refreshToken={}",
        request.refreshToken());
    logoutUseCase.execute(request.refreshToken());
    return Response.noContent().build();
  }
}
