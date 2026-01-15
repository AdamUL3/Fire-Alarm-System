package ca.ulaval.glo4002.application.domain.action;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.user.PersonName;
import ca.ulaval.glo4002.application.domain.user.PhoneNumber;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomActionFactoryTest {
  private RoomActionFactory roomActionFactory;

  @Mock private BuildingID buildingId;
  @Mock private ZoneID zoneId;
  @Mock private RoomID roomId;
  @Mock private PersonID personId;
  @Mock private PhoneNumber phoneNumber;
  @Mock private PersonName personName;

  @BeforeEach
  void setUp() {
    roomActionFactory = new RoomActionFactory();
  }

  @Test
  void givenPhoneNumber_whenCreateSendPredefinedSMSMessage_thenDestinationNumberIsSet() {
    Action result =
        roomActionFactory.createSendPredefinedSMSMessage(
            phoneNumber, personName, CodeMessageType.FIRE_ALERT_IN_ROOM);
    SendSMSMessageAction returnedAction = (SendSMSMessageAction) result;
    assertSame(phoneNumber, returnedAction.getDestinationNumber());
  }

  @Test
  void givenPersonName_whenCreateSendPredefinedSMSMessage_thenDestinationNameIsSet() {
    Action result =
        roomActionFactory.createSendPredefinedSMSMessage(
            phoneNumber, personName, CodeMessageType.FIRE_ALERT_IN_ROOM);
    SendSMSMessageAction returnedAction = (SendSMSMessageAction) result;
    assertSame(personName, returnedAction.getSupervisorName());
  }

  @Test
  void givenCodeMessage_whenCreateSendPredefinedSMSMessage_thenCodeMessageIsSet() {
    Action result =
        roomActionFactory.createSendPredefinedSMSMessage(
            phoneNumber, personName, CodeMessageType.FIRE_ALERT_IN_ROOM);
    SendSMSMessageAction returnedAction = (SendSMSMessageAction) result;
    assertSame(CodeMessageType.FIRE_ALERT_IN_ROOM, returnedAction.getMessageType());
  }

  @Test
  void givenState_whenCreateOpenClosePower_thenStateIsSet() {
    Action result =
        roomActionFactory.createOpenClosePower(buildingId, zoneId, roomId, OpenClosedState.CLOSE);
    OpenClosePowerAction returnedAction = (OpenClosePowerAction) result;
    assertSame(OpenClosedState.CLOSE, returnedAction.getElectricityState());
  }

  @Test
  void givenPersonId_whenCreateSendPredefinedTeamsMessage_thenDestinationIdIsSet() {
    Action result =
        roomActionFactory.createSendPredefinedTeamsMessage(
            personId, CodeMessageType.FIRE_ALERT_IN_ROOM);
    SendTeamsMessageAction returnedAction = (SendTeamsMessageAction) result;
    assertSame(personId, returnedAction.getDestinationId());
  }

  @Test
  void givenCodeMessage_whenCreateSendPredefinedTeamsMessage_thenCodeMessageIsSet() {
    Action result =
        roomActionFactory.createSendPredefinedTeamsMessage(
            personId, CodeMessageType.FIRE_ALERT_IN_ROOM);
    SendTeamsMessageAction returnedAction = (SendTeamsMessageAction) result;
    assertSame(CodeMessageType.FIRE_ALERT_IN_ROOM, returnedAction.getMessageType());
  }
}
