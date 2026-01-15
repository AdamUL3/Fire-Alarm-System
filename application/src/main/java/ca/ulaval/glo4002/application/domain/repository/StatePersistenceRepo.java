package ca.ulaval.glo4002.application.domain.repository;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.Optional;

public interface StatePersistenceRepo {
  Optional<AggregateBuildingState> findCurrentStateByBuildingId(BuildingID id);

  Optional<AggregateBuildingState> findInitialStateByBuildingAndZoneId(
      BuildingID id, ZoneID zoneID);

  void saveCurrentState(Building building);

  void saveInitialState(Building building, ZoneID zoneID);

  void deleteInitialState(BuildingID id, ZoneID zoneID);

  void resetDatabase();
}
