package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.BuildingStateEntity;
import java.util.Optional;

public class SQLiteStateRepo implements StatePersistenceRepo {
  private final SQLiteQueryDAO queryService;
  private final BuildingStateMapper stateMapper;

  public SQLiteStateRepo(SQLiteQueryDAO queryService, BuildingStateMapper stateMapper) {
    this.queryService = queryService;
    this.stateMapper = stateMapper;
  }

  @Override
  public Optional<AggregateBuildingState> findCurrentStateByBuildingId(BuildingID id) {
    Optional<BuildingStateEntity> entity = queryService.findCurrentStateByBuildingId(id.value());
    return entity.map(stateMapper::toAggregate);
  }

  @Override
  public Optional<AggregateBuildingState> findInitialStateByBuildingAndZoneId(
      BuildingID id, ZoneID zoneID) {
    Optional<BuildingStateEntity> entity =
        queryService.findInitialStateByBuildingAndZoneId(id, zoneID);
    return entity.map(stateMapper::toAggregate);
  }

  @Override
  public void saveCurrentState(Building building) {
    if (!queryService.saveCurrentState(stateMapper.toStateEntity(building)))
      throw new PersistenceException("Failed to save building " + building.getId().value());
  }

  @Override
  public void saveInitialState(Building building, ZoneID zoneID) {
    if (!queryService.saveInitialState(stateMapper.toStateEntity(building), zoneID)) {
      throw new PersistenceException("Failed to save building " + building.getId().value());
    }
  }

  @Override
  public void deleteInitialState(BuildingID id, ZoneID zoneID) {
    if (!queryService.deleteInitialState(id, zoneID)) {
      throw new PersistenceException("Failed to save building " + id.value());
    }
  }

  @Override
  public void resetDatabase() {
    queryService.resetDatabase();
  }
}
