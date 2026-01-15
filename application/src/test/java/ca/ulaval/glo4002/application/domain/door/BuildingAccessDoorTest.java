package ca.ulaval.glo4002.application.domain.door;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuildingAccessDoorTest {

  private BuildingAccessDoor buildingAccessDoor;
  private final DoorID doorId = new DoorID("D1");

  @Mock private DoorState doorState;
  @Mock private DoorState initialDoorState;
  @Mock private DoorActionFactory doorActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private PersonAccessInformation personAccessInformation;
  @Mock private Action returnedAction;

  @BeforeEach
  void setUp() {
    buildingAccessDoor = new BuildingAccessDoor(doorId, doorState, false, doorActionFactory);
  }

  @Test
  void givenSecurityAgent_whenHandleAccessDuringEmergency_thenDoesNotThrow() {
    when(personAccessInformation.getRole()).thenReturn(AccessRole.SECURITY_AGENT);

    assertDoesNotThrow(
        () -> buildingAccessDoor.handleAccessDuringEmergency(personAccessInformation));
  }

  @Test
  void givenLabResponsible_whenHandleAccessDuringEmergency_thenDoesNotThrow() {
    when(personAccessInformation.isLabResponsibleInBuilding()).thenReturn(true);

    assertDoesNotThrow(
        () -> buildingAccessDoor.handleAccessDuringEmergency(personAccessInformation));
  }

  @Test
  void givenUnauthorizedPerson_whenHandleAccessDuringEmergency_thenThrowsRefusedAccessException() {
    when(personAccessInformation.getRole()).thenReturn(AccessRole.REGULAR);
    when(personAccessInformation.isLabResponsibleInBuilding()).thenReturn(false);

    assertThrows(
        RefusedAccessException.class,
        () -> buildingAccessDoor.handleAccessDuringEmergency(personAccessInformation));
  }

  @Test
  void whenHandleFireAlarm_thenAddsLockUnlockAction() {
    when(actionContext.getBuildingID()).thenReturn(new BuildingID("B1"));
    when(actionContext.getZoneID()).thenReturn(new ZoneID("Z1"));
    when(doorActionFactory.createLockUnlockDoor(
            actionContext.getBuildingID(),
            actionContext.getZoneID(),
            doorId,
            LockedUnlockedState.LOCKED))
        .thenReturn(returnedAction);

    buildingAccessDoor.handleFireAlarm(actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void whenHandleFireAlarm_thenAddsOpenCloseAction() {
    when(actionContext.getBuildingID()).thenReturn(new BuildingID("B1"));
    when(actionContext.getZoneID()).thenReturn(new ZoneID("Z1"));
    when(doorActionFactory.createOpenCloseDoor(
            actionContext.getBuildingID(),
            actionContext.getZoneID(),
            doorId,
            OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    buildingAccessDoor.handleFireAlarm(actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenLockStateChanged_whenHandleFireAlarmCompleted_thenRevertsLockState() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.UNLOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(actionContext.getBuildingID()).thenReturn(new BuildingID("B1"));
    when(actionContext.getZoneID()).thenReturn(new ZoneID("Z1"));
    when(doorActionFactory.createLockUnlockDoor(
            actionContext.getBuildingID(),
            actionContext.getZoneID(),
            doorId,
            LockedUnlockedState.LOCKED))
        .thenReturn(returnedAction);

    buildingAccessDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenLockStateNotChanged_whenHandleFireAlarmCompleted_thenDoesNotAddLockAction() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);

    buildingAccessDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorActionFactory, never()).createLockUnlockDoor(any(), any(), any(), any());
  }

  @Test
  void givenOpenCloseStateChanged_whenHandleFireAlarmCompleted_thenRevertsOpenCloseState() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.OPEN);
    when(initialDoorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);
    when(actionContext.getBuildingID()).thenReturn(new BuildingID("B1"));
    when(actionContext.getZoneID()).thenReturn(new ZoneID("Z1"));
    when(doorActionFactory.createOpenCloseDoor(
            actionContext.getBuildingID(),
            actionContext.getZoneID(),
            doorId,
            OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    buildingAccessDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenOpenCloseStateNotChanged_whenHandleFireAlarmCompleted_thenDoesNotAddOpenCloseAction() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);
    when(initialDoorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);

    buildingAccessDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorActionFactory, never()).createOpenCloseDoor(any(), any(), any(), any());
  }
}
