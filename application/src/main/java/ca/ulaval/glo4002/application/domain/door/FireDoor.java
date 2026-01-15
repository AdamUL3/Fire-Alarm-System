package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class FireDoor extends LockableDoor {

  public FireDoor(
      DoorID id, DoorState state, boolean isOnEvacuationPath, DoorActionFactory doorActionFactory) {
    super(id, state, isOnEvacuationPath, doorActionFactory);
  }

  @Override
  public void handleFireAlarmCompleted(ActionContext actionContext, DoorState initialDoorState) {
    super.handleFireAlarmCompleted(actionContext, initialDoorState);
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

  @Override
  public void handleFireAlarm(ActionContext actionContext) {
    super.handleFireAlarm(actionContext);
    if (!state.getOpenClosedState().isOpen()) {
      actionContext.addAction(open(actionContext.getBuildingID(), actionContext.getZoneID()));
    }
  }

  private Action open(BuildingID buildingId, ZoneID zoneId) {
    state.setOpenClosedState(OpenClosedState.OPEN);
    return doorActionFactory.createOpenCloseDoor(buildingId, zoneId, id, OpenClosedState.OPEN);
  }

  public void handleProtest(ActionContext actionContext) {}
}
