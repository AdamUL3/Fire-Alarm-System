package ca.ulaval.glo4002.application.domain.repository;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public interface BuildingMapRepo {
  Building findByZoneId(ZoneID zone);

  AggregateBuildingState findBuildingStateByBuildingId(BuildingID id);
}
