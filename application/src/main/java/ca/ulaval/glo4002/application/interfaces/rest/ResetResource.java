package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.application.ResetService;
import ca.ulaval.glo4002.application.infrastructure.ApplicationFactory;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reinitialiser")
@Produces(MediaType.APPLICATION_JSON)
public class ResetResource {
  private final ResetService resetService = ApplicationFactory.createResetService();

  @POST
  public Response reset() {
    resetService.resetDatabase();
    return Response.status(Response.Status.OK).build();
  }
}
