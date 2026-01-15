package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.application.AccessService;
import ca.ulaval.glo4002.application.infrastructure.ApplicationFactory;
import ca.ulaval.glo4002.application.interfaces.rest.dto.AccessRequestDTO;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.AccessRequestMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/demande-acces")
@Consumes(MediaType.APPLICATION_JSON)
public class AccessResource {

  private final AccessService accessService = ApplicationFactory.createAccessService();
  private final AccessRequestMapper accessRequestMapper = new AccessRequestMapper();

  @POST
  public Response AccessRequest(AccessRequestDTO accessRequestDTO) {
    accessService.handleAccessRequest(accessRequestMapper.toAccessRequest(accessRequestDTO));
    return Response.ok().build();
  }
}
