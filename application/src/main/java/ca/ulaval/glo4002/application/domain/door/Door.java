package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.DoorActionFactory;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;

public class Door {
  protected final DoorID id;
  protected final DoorState state;
  protected boolean isOnEvacuationPath;
  protected DoorActionFactory doorActionFactory;

  public Door(
      DoorID id, DoorState state, boolean isOnEvacuationPath, DoorActionFactory doorActionFactory) {
    this.id = id;
    this.state = state;
    this.isOnEvacuationPath = isOnEvacuationPath;
    this.doorActionFactory = doorActionFactory;
  }

  public DoorID getId() {
    return id;
  }

  public DoorState getState() {
    return state;
  }

  public void handleFireAlarmCompleted(ActionContext actionContext, DoorState initialDoorState) {}

  public void handleFireAlarm(ActionContext actionContext) {}

  public void handleProtest(ActionContext actionContext) {}

  public void handleAccessDuringEmergency(PersonAccessInformation accessCardInformation) {}
}
