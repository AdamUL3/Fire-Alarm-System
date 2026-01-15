package ca.ulaval.glo4002.application.domain.building.exception;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.exception.DomainException;

public class BuildingNotFoundException extends DomainException {
  public BuildingNotFoundException(BuildingID buildingId) {
    super(
        String.format("Building with ID '%s' not found", buildingId.value()), "BUILDING_NOT_FOUND");
  }
}
