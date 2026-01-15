package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.interfaces.rest.dto.BuildingStateDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuildingStateResponse {

  @JsonProperty BuildingStateDTO buildingState;

  public BuildingStateResponse(BuildingStateDTO buildingState) {
    this.buildingState = buildingState;
  }
}
