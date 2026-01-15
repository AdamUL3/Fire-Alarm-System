package ca.ulaval.glo4002.application.domain.room;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LaboratoryTest {

  private static final String ROOM_ID_STRING = "R1";
  private static final String LAB_PERSON_ID_STRING = "P1";
  private static final String OTHER_PERSON_ID_STRING = "P2";
  private static final String LAB_RESPONSIBLE_NAME = "John Doe";
  private static final String LAB_RESPONSIBLE_PHONE = "1234567890";

  private RoomID roomId;
  private PersonID labPersonID;
  private PersonID otherPersonID;

  @Mock private RoomState roomState;
  @Mock private RoomActionFactory roomActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private RoomState initialRoomState;
  @Mock private PersonAccessInformation accessInfo;

  @BeforeEach
  void setUp() {
    roomId = new RoomID(ROOM_ID_STRING);
    labPersonID = new PersonID(LAB_PERSON_ID_STRING);
    otherPersonID = new PersonID(OTHER_PERSON_ID_STRING);
  }

  @Test
  void givenResponsibleAvailable_whenHandleFireAlarm_thenSendSMS() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarm(actionContext);

    verify(roomActionFactory)
        .createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(),
            labResponsible.name(),
            CodeMessageType.FIRE_ALERT_IN_ROOM);
  }

  @Test
  void givenResponsibleAvailable_whenHandleFireAlarm_thenSendTeamsMessage() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarm(actionContext);

    verify(roomActionFactory)
        .createSendPredefinedTeamsMessage(labResponsible.id(), CodeMessageType.FIRE_ALERT_IN_ROOM);
  }

  @Test
  void givenResponsibleUnavailable_whenHandleFireAlarm_thenSendSMSOnly() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.OUT_OF_OFFICE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarm(actionContext);

    verify(roomActionFactory)
        .createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(),
            labResponsible.name(),
            CodeMessageType.FIRE_ALERT_IN_ROOM);
  }

  @Test
  void givenResponsibleAvailable_whenHandleFireAlarmCompleted_thenSendSMS() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarmCompleted(actionContext, initialRoomState);

    verify(roomActionFactory)
        .createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(), labResponsible.name(), CodeMessageType.END);
  }

  @Test
  void givenResponsibleAvailable_whenHandleFireAlarmCompleted_thenSendTeamsMessage() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarmCompleted(actionContext, initialRoomState);

    verify(roomActionFactory)
        .createSendPredefinedTeamsMessage(labResponsible.id(), CodeMessageType.END);
  }

  @Test
  void givenResponsibleUnavailable_whenHandleFireAlarmCompleted_thenSendSMSOnly() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.OUT_OF_OFFICE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    laboratory.handleFireAlarmCompleted(actionContext, initialRoomState);

    verify(roomActionFactory)
        .createSendPredefinedSMSMessage(
            labResponsible.phoneNumber(), labResponsible.name(), CodeMessageType.END);
  }

  @Test
  void givenPersonIsLabResponsible_whenIsLabResponsible_thenReturnTrue() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    assertTrue(laboratory.isLabResponsible(labPersonID));
  }

  @Test
  void givenPersonIsNotLabResponsible_whenIsLabResponsible_thenReturnFalse() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    assertFalse(laboratory.isLabResponsible(otherPersonID));
  }

  @Test
  void givenPersonIsLabResponsible_whenHandleAccessDuringEmergency_thenDoNotThrow() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    when(accessInfo.getPersonId()).thenReturn(labPersonID);

    assertDoesNotThrow(() -> laboratory.handleAccessDuringEmergency(accessInfo));
  }

  @Test
  void
      givenPersonIsNotLabResponsible_whenHandleAccessDuringEmergency_thenThrowRefusedAccessException() {
    PersonInformation labResponsible =
        new PersonInformation(
            new PersonName(LAB_RESPONSIBLE_NAME),
            new PhoneNumber(LAB_RESPONSIBLE_PHONE),
            labPersonID,
            ResponsibleStatus.AVAILABLE);
    Laboratory laboratory = new Laboratory(roomId, roomState, roomActionFactory, labResponsible);

    when(accessInfo.getPersonId()).thenReturn(otherPersonID);

    assertThrows(
        RefusedAccessException.class, () -> laboratory.handleAccessDuringEmergency(accessInfo));
  }
}
