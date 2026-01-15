package ca.ulaval.glo4002.application.infrastructure.persistence.mappers;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.AggregateZoneState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.BuildingDTO;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.BuildingStateEntity;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.DoorStateEntity;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.RoomStateEntity;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.ZoneStateEntity;
import ca.ulaval.glo4002.application.interfaces.rest.dto.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildingStateMapper {
  private final ZoneStateMapper zoneStateMapper;
  private final DoorStateMapper doorStateMapper;
  private final RoomStateMapper roomStateMapper;

  public BuildingStateMapper(
      ZoneStateMapper zoneStateMapper,
      DoorStateMapper doorStateMapper,
      RoomStateMapper roomStateMapper) {
    this.zoneStateMapper = zoneStateMapper;
    this.doorStateMapper = doorStateMapper;
    this.roomStateMapper = roomStateMapper;
  }

  public BuildingStateEntity toStateEntity(Building building) {
    List<ZoneStateEntity> zones = new ArrayList<>();

    building
        .getZones()
        .forEach(
            zone -> {
              List<DoorStateEntity> doors =
                  zone.getDoors().stream()
                      .map(door -> doorStateMapper.toStateEntity(door.getId(), door.getState()))
                      .toList();

              List<RoomStateEntity> rooms =
                  zone.getRooms().stream()
                      .map(room -> roomStateMapper.toStateEntity(room.getId(), room.getState()))
                      .toList();

              zones.add(zoneStateMapper.toStateEntity(zone.getId(), zone.getState(), doors, rooms));
            });

    return new BuildingStateEntity(building.getId(), zones);
  }

  public BuildingStateDTO toDTO(AggregateBuildingState buildingState) {
    List<ZoneStateDTO> zones = new ArrayList<>();
    List<DoorStateDTO> doors = new ArrayList<>();
    AtomicInteger numberOfAgents = new AtomicInteger();

    buildingState
        .zoneStates()
        .forEach(
            (id, zone) -> {
              zone.doorStates()
                  .forEach(
                      (doorId, state) -> {
                        doors.add(
                            new DoorStateDTO(
                                doorId.value(),
                                state.getOpenClosedState().isOpen(),
                                state.getLockUnlockState().isLocked()));
                      });
              zones.add(zoneStateMapper.toDTO(id, zone.zoneState(), zone.roomStates()));
              numberOfAgents.set(numberOfAgents.get() + zone.zoneState().getNumberOfAgents());
            });

    AgentsStateDTO agentsStateDTO = new AgentsStateDTO(numberOfAgents);

    return new BuildingStateDTO(buildingState.id().value(), agentsStateDTO, zones, doors);
  }

  public AggregateBuildingState toAggregate(BuildingStateEntity buildingStateEntity) {
    if (buildingStateEntity == null) {
      return null;
    }
    Map<ZoneID, AggregateZoneState> zoneStates = new HashMap<>();

    buildingStateEntity
        .zones()
        .forEach(
            zoneState -> {
              zoneStates.put(zoneState.id(), zoneStateMapper.toAggregate(zoneState));
            });

    return new AggregateBuildingState(buildingStateEntity.id(), zoneStates);
  }

  public AggregateBuildingState toAggregate(BuildingDTO buildingDTO) {
    if (buildingDTO == null) {
      return null;
    }

    Map<ZoneID, AggregateZoneState> zoneStates = new HashMap<>();

    buildingDTO
        .zones()
        .forEach(
            dto -> {
              zoneStates.put(
                  new ZoneID(dto.id()),
                  zoneStateMapper.toAggregate(dto, buildingDTO.ventilationSystem()));
            });

    return new AggregateBuildingState(new BuildingID(buildingDTO.id()), zoneStates);
  }
}
