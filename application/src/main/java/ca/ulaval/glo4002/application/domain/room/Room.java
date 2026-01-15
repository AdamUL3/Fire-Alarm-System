package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;

public class Room {
  protected final RoomID id;
  protected final RoomState state;
  protected final RoomActionFactory roomActionFactory;

  public Room(RoomID id, RoomState state, RoomActionFactory roomActionFactory) {
    this.id = id;
    this.state = state;
    this.roomActionFactory = roomActionFactory;
  }

  public RoomID getId() {
    return id;
  }

  public RoomState getState() {
    return state;
  }

  public void handleFireAlarmCompleted(ActionContext actionContext, RoomState initialRoomState) {}

  public void handleFireAlarm(ActionContext actionContext) {}

  public void handleCardEntry(PersonID personID) {
    state.addOccupant(personID);
  }

  public Intervention handleNoCardEntry(ActionContext actionContext) {
    state.addOccupant(null);
    return null;
  }

  public void handleExitRoom() {
    state.removeOccupant();
  }

  public void handleAccessDuringEmergency(PersonAccessInformation accessCardInformation) {
    throw new RefusedAccessException();
  }

  public boolean isLabResponsible(PersonID personID) {
    return false;
  }

  public boolean consecutiveAttemptSuccess(PersonAccessInformation accessCardInformation) {
    if (accessCardInformation.getRole() != AccessRole.SECURITY_AGENT) {
      return false;
    }
    return state.registerConsecutiveAccess(accessCardInformation.getPersonId());
  }

  public void addOccupant(PersonID personID) {
    state.addOccupant(personID);
  }
}
