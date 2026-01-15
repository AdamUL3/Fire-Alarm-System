package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.DoorActionFactory;
import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class LockableDoor extends Door {
  public LockableDoor(
      DoorID id, DoorState state, boolean isOnEvacuationPath, DoorActionFactory doorActionFactory) {
    super(id, state, isOnEvacuationPath, doorActionFactory);
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
  }

  @Override
  public void handleFireAlarm(ActionContext actionContext) {
    if (isOnEvacuationPath && state.getLockUnlockState().isLocked())
      actionContext.addAction(unlock(actionContext.getBuildingID(), actionContext.getZoneID()));
  }

  private Action unlock(BuildingID buildingId, ZoneID zoneId) {
    state.setLockUnlockState(LockedUnlockedState.UNLOCKED);
    return doorActionFactory.createLockUnlockDoor(
        buildingId, zoneId, id, LockedUnlockedState.UNLOCKED);
  }

  private Action lock(BuildingID buildingId, ZoneID zoneId) {
    state.setLockUnlockState(LockedUnlockedState.LOCKED);
    return doorActionFactory.createLockUnlockDoor(
        buildingId, zoneId, id, LockedUnlockedState.LOCKED);
  }

  public void handleProtest(ActionContext actionContext) {
    actionContext.addAction(lock(actionContext.getBuildingID(), actionContext.getZoneID()));
  }
}
