package ca.ulaval.glo4002.application.domain.room;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomTest {

  private Room room;

  @Mock private RoomID roomId;
  @Mock private RoomState roomState;
  @Mock private RoomActionFactory roomActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private PersonID personID;
  @Mock private PersonAccessInformation accessInfo;

  @BeforeEach
  void setup() {
    room = new Room(roomId, roomState, roomActionFactory);
  }

  @Test
  void givenRoom_whenHandleFireAlarm_thenDoesNothing() {
    room.handleFireAlarm(actionContext);
  }

  @Test
  void givenRoom_whenHandleFireAlarmCompleted_thenDoesNothing() {
    room.handleFireAlarmCompleted(actionContext, roomState);
  }

  @Test
  void givenRoom_whenHandleCardEntry_thenAddsIdentifiedOccupant() {
    room.handleCardEntry(personID);

    verify(roomState).addOccupant(personID);
  }

  @Test
  void givenRoom_whenHandleNoCardEntry_thenAddsNullOccupant() {
    room.handleNoCardEntry(actionContext);

    verify(roomState).addOccupant(null);
  }

  @Test
  void givenRoom_whenHandleExitRoom_thenRemovesOccupant() {
    room.handleExitRoom();

    verify(roomState).removeOccupant();
  }

  @Test
  void givenNonLabResponsible_whenHandleAccessDuringEmergency_thenThrowsRefusedAccessException() {
    assertThrows(RefusedAccessException.class, () -> room.handleAccessDuringEmergency(accessInfo));
  }

  @Test
  void givenAccessCardWithNonSecurityRole_whenConsecutiveAttemptSuccess_thenReturnsFalse() {
    when(accessInfo.getRole()).thenReturn(AccessRole.REGULAR);

    boolean result = room.consecutiveAttemptSuccess(accessInfo);

    assertFalse(result);
  }

  @Test
  void givenAccessCardWithSecurityRole_whenConsecutiveAttemptSuccess_thenRegistersAccessInState() {
    when(accessInfo.getRole()).thenReturn(AccessRole.SECURITY_AGENT);
    when(accessInfo.getPersonId()).thenReturn(personID);
    when(roomState.registerConsecutiveAccess(personID)).thenReturn(true);

    boolean result = room.consecutiveAttemptSuccess(accessInfo);

    assertTrue(result);
  }

  @Test
  void
      givenAccessCardWithSecurityRole_whenConsecutiveAttemptSuccess_thenRegistersAccessInStateFalse() {
    when(accessInfo.getRole()).thenReturn(AccessRole.SECURITY_AGENT);
    when(accessInfo.getPersonId()).thenReturn(personID);
    when(roomState.registerConsecutiveAccess(personID)).thenReturn(false);

    boolean result = room.consecutiveAttemptSuccess(accessInfo);

    assertFalse(result);
  }

  @Test
  void givenRoom_whenAddOccupant_thenAddsOccupantInState() {
    room.addOccupant(personID);

    verify(roomState).addOccupant(personID);
  }

  @Test
  void givenRoom_whenIsLabResponsible_thenReturnsFalse() {
    boolean result = room.isLabResponsible(personID);

    assertFalse(result);
  }
}
