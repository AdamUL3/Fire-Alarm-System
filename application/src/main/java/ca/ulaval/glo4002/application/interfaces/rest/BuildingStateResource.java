package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.application.BuildingStateService;
import ca.ulaval.glo4002.application.infrastructure.ApplicationFactory;
import ca.ulaval.glo4002.application.interfaces.rest.dto.BuildingStateDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/v2/emergencies/0/buildings-state")
@Produces(MediaType.APPLICATION_JSON)
public class BuildingStateResource {

  BuildingStateService buildingStateService = ApplicationFactory.createBuildingStateService();

  @GET
  @Path("/{building-id}")
  public BuildingStateResponse getBuildingState(@PathParam("building-id") String buildingId) {
    try {
      BuildingStateDTO buildingState = buildingStateService.findByBuildingId(buildingId);
      return new BuildingStateResponse(buildingState);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
