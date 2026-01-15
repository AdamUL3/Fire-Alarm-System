package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.CodeMessageType;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.user.PersonInformation;
import ca.ulaval.glo4002.application.domain.user.ResponsibleStatus;

public class Laboratory extends Room {
  private final PersonInformation labResponsible;

  public Laboratory(
      RoomID id,
      RoomState state,
      RoomActionFactory roomActionFactory,
      PersonInformation labResponsible) {
    super(id, state, roomActionFactory);
    this.labResponsible = labResponsible;
  }

  @Override
  public void handleFireAlarmCompleted(ActionContext actionContext, RoomState initialRoomState) {
    actionContext.addAction(
        roomActionFactory.createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(), labResponsible.name(), CodeMessageType.END));
    if (labResponsible.status() == ResponsibleStatus.AVAILABLE) {
      actionContext.addAction(
          roomActionFactory.createSendPredefinedTeamsMessage(
              labResponsible.id(), CodeMessageType.END));
    }
  }

  @Override
  public void handleFireAlarm(ActionContext actionContext) {
    actionContext.addAction(
        roomActionFactory.createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(),
            labResponsible.name(),
            CodeMessageType.FIRE_ALERT_IN_ROOM));

    if (labResponsible.status() == ResponsibleStatus.AVAILABLE) {
      actionContext.addAction(
          roomActionFactory.createSendPredefinedTeamsMessage(
              labResponsible.id(), CodeMessageType.FIRE_ALERT_IN_ROOM));
    }
  }

  @Override
  public boolean isLabResponsible(PersonID personID) {
    return labResponsible.id().equals(personID);
  }

  @Override
  public void handleAccessDuringEmergency(PersonAccessInformation personAccessInfo) {
    if (labResponsible.id().equals(personAccessInfo.getPersonId())) {
      return;
    }
    throw new RefusedAccessException();
  }
}
