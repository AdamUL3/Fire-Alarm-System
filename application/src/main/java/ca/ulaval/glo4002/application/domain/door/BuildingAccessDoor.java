package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.DoorActionFactory;
import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;

public class BuildingAccessDoor extends LockableDoor {
  public BuildingAccessDoor(
      DoorID id, DoorState state, boolean isOnEvacuationPath, DoorActionFactory doorActionFactory) {
    super(id, state, isOnEvacuationPath, doorActionFactory);
  }

  @Override
  public void handleAccessDuringEmergency(PersonAccessInformation personAccessInformation) {
    if (personAccessInformation.getRole() == AccessRole.SECURITY_AGENT) {
      return;
    } else if (personAccessInformation.isLabResponsibleInBuilding()) {
      return;
    }
    throw new RefusedAccessException();
  }

  @Override
  public void handleFireAlarm(ActionContext actionContext) {
    actionContext.addAction(
        doorActionFactory.createLockUnlockDoor(
            actionContext.getBuildingID(),
            actionContext.getZoneID(),
            id,
            LockedUnlockedState.LOCKED));
    actionContext.addAction(
        doorActionFactory.createOpenCloseDoor(
            actionContext.getBuildingID(), actionContext.getZoneID(), id, OpenClosedState.CLOSE));
  }

  @Override
  public void handleFireAlarmCompleted(ActionContext actionContext, DoorState initialDoorState) {
    if (state.getLockUnlockState() != initialDoorState.getLockUnlockState()) {
      state.setLockUnlockState(initialDoorState.getLockUnlockState());
      actionContext.addAction(
          doorActionFactory.createLockUnlockDoor(
              actionContext.getBuildingID(),
              actionContext.getZoneID(),
              id,
              initialDoorState.getLockUnlockState()));
    }
    if (state.getOpenClosedState() != initialDoorState.getOpenClosedState()) {
      state.setOpenClosedState(initialDoorState.getOpenClosedState());
      actionContext.addAction(
          doorActionFactory.createOpenCloseDoor(
              actionContext.getBuildingID(),
              actionContext.getZoneID(),
              id,
              initialDoorState.getOpenClosedState()));
    }
  }
}
