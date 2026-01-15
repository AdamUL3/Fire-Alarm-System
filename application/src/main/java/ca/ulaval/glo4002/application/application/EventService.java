package ca.ulaval.glo4002.application.application;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.event.Event;
import ca.ulaval.glo4002.application.domain.event.EventContext;
import ca.ulaval.glo4002.application.domain.repository.BuildingMapRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;
import java.util.Optional;

public class EventService {
  private final StatePersistenceRepo statePersistenceRepo;
  private final BuildingMapRepo buildingMapService;

  public EventService(
      StatePersistenceRepo statePersistenceRepo, BuildingMapRepo buildingMapService) {
    this.statePersistenceRepo = statePersistenceRepo;
    this.buildingMapService = buildingMapService;
  }

  public List<Action> handleEvent(Event event) {
    ZoneID zoneId = event.getZone();
    Building building = buildingMapService.findByZoneId(zoneId);

    Optional<AggregateBuildingState> initialState =
        statePersistenceRepo.findInitialStateByBuildingAndZoneId(building.getId(), zoneId);

    if (event.requiresInitialCapture() && initialState.isEmpty()) {
      statePersistenceRepo.saveInitialState(building, zoneId);
      initialState =
          statePersistenceRepo.findInitialStateByBuildingAndZoneId(building.getId(), zoneId);
    }

    EventContext eventContext = new EventContext(zoneId, initialState);

    List<Action> actions = event.handleEvent(building, eventContext);

    statePersistenceRepo.saveCurrentState(building);

    if (event.endsIncident()) {
      statePersistenceRepo.deleteInitialState(building.getId(), zoneId);
    }

    return actions;
  }
}
