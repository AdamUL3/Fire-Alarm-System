package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.user.PersonName;
import ca.ulaval.glo4002.application.domain.user.PhoneNumber;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class RoomActionFactory {
  public Action createSendPredefinedSMSMessage(
      PhoneNumber destinationNum, PersonName destinationName, CodeMessageType codeMessage) {
    return new SendSMSMessageAction(destinationNum, destinationName, codeMessage);
  }

  public Action createOpenClosePower(
      BuildingID building, ZoneID zone, RoomID room, OpenClosedState state) {
    return new OpenClosePowerAction(building, zone, room, state);
  }

  public Action createSendPredefinedTeamsMessage(
      PersonID destinationId, CodeMessageType codeMessage) {
    return new SendTeamsMessageAction(destinationId, codeMessage);
  }
}
