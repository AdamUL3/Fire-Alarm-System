package ca.ulaval.glo4002.application.interfaces.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/heartbeat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource {
  private final Logger logger = LoggerFactory.getLogger(HeartbeatResource.class);

  @GET
  public HeartbeatResponse heartbeat(@QueryParam("token") @NotNull String token) {
    logger.info("Received heartbeat : " + token);
    return new HeartbeatResponse(token);
  }
}
