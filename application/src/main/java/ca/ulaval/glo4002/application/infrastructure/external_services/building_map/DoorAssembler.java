package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.action.DoorActionFactory;
import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.door.*;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.DoorDTO;

public class DoorAssembler {
  private final DoorActionFactory doorActionFactory;

  public DoorAssembler(DoorActionFactory actionFactory) {
    this.doorActionFactory = actionFactory;
  }

  public Door toDoor(DoorDTO doorDTO, DoorState doorState) {
    if (doorState != null) return createDoorWithPreviousState(doorDTO, doorState);
    else return createDoorWithoutPreviousState(doorDTO);
  }

  private Door createDoorWithoutPreviousState(DoorDTO doorDTO) {
    DoorID doorID = new DoorID(doorDTO.id());
    DoorState doorState =
        new DoorState(
            OpenClosedState.getState(doorDTO.isOpen()),
            LockedUnlockedState.getState(doorDTO.isLocked()));
    return getDoor(doorDTO, doorID, doorState);
  }

  private Door createDoorWithPreviousState(DoorDTO doorDTO, DoorState doorState) {
    DoorID doorID = new DoorID(doorDTO.id());
    return getDoor(doorDTO, doorID, doorState);
  }

  private Door getDoor(DoorDTO doorDTO, DoorID doorID, DoorState doorState) {
    return switch (doorDTO.type()) {
      case "FIRE_DOOR" ->
          new FireDoor(doorID, doorState, doorDTO.onEvacuationPath(), doorActionFactory);
      case "LOCKABLE" ->
          new LockableDoor(doorID, doorState, doorDTO.onEvacuationPath(), doorActionFactory);
      case "BUILDING_ACCESS" ->
          new BuildingAccessDoor(doorID, doorState, doorDTO.onEvacuationPath(), doorActionFactory);
      default -> new Door(doorID, doorState, doorDTO.onEvacuationPath(), doorActionFactory);
    };
  }
}
