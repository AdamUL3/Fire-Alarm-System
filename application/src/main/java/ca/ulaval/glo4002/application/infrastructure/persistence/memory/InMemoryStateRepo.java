package ca.ulaval.glo4002.application.infrastructure.persistence.memory;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.BuildingStateEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryStateRepo implements StatePersistenceRepo {

  private final Map<BuildingID, BuildingStateEntity> currentStore = new HashMap<>();
  private final Map<ZoneID, BuildingStateEntity> initialStore = new HashMap<>();
  private final BuildingStateMapper stateMapper;

  public InMemoryStateRepo(BuildingStateMapper stateMapper) {
    this.stateMapper = stateMapper;
  }

  @Override
  public Optional<AggregateBuildingState> findCurrentStateByBuildingId(BuildingID id) {
    return Optional.ofNullable(stateMapper.toAggregate(currentStore.get(id)));
  }

  @Override
  public Optional<AggregateBuildingState> findInitialStateByBuildingAndZoneId(
      BuildingID id, ZoneID zoneID) {
    return Optional.ofNullable(stateMapper.toAggregate(initialStore.getOrDefault(zoneID, null)));
  }

  @Override
  public void saveCurrentState(Building building) {
    currentStore.put(building.getId(), stateMapper.toStateEntity(building));
  }

  @Override
  public void saveInitialState(Building building, ZoneID zoneID) {
    initialStore.put(zoneID, stateMapper.toStateEntity(building));
  }

  @Override
  public void deleteInitialState(BuildingID id, ZoneID zoneID) {
    initialStore.remove(zoneID);
  }

  @Override
  public void resetDatabase() {
    currentStore.clear();
    initialStore.clear();
  }
}
