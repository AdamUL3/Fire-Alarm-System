package ca.ulaval.glo4002.application.infrastructure.persistence.mappers;

import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.door.DoorState;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.DoorStateEntity;

public class DoorStateMapper {
  public DoorStateEntity toStateEntity(DoorID id, DoorState door) {
    return new DoorStateEntity(
        id, door.getOpenClosedState().isOpen(), door.getLockUnlockState().isLocked());
  }

  public DoorState toState(DoorStateEntity doorStateEntity) {
    return new DoorState(
        OpenClosedState.getState(doorStateEntity.openClosedState()),
        LockedUnlockedState.getState(doorStateEntity.lockUnlockState()));
  }
}
