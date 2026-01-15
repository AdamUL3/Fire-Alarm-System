package ca.ulaval.glo4002.application.application;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.repository.BuildingMapRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import ca.ulaval.glo4002.application.interfaces.rest.dto.BuildingStateDTO;
import java.util.Optional;

public class BuildingStateService {
  private final StatePersistenceRepo stateRepo;
  private final BuildingMapRepo buildingMapRepo;
  private final BuildingStateMapper stateMapper;

  public BuildingStateService(
      StatePersistenceRepo stateRepo,
      BuildingMapRepo buildingMapRepo,
      BuildingStateMapper stateMapper) {
    this.stateRepo = stateRepo;
    this.buildingMapRepo = buildingMapRepo;
    this.stateMapper = stateMapper;
  }

  public BuildingStateDTO findByBuildingId(String id) {
    BuildingID buildingId = new BuildingID(id);

    Optional<AggregateBuildingState> buildingState =
        stateRepo.findCurrentStateByBuildingId(buildingId);

    AggregateBuildingState state =
        buildingState.orElse(buildingMapRepo.findBuildingStateByBuildingId(buildingId));

    return stateMapper.toDTO(state);
  }
}
