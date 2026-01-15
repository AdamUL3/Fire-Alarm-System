package ca.ulaval.glo4002.application.domain.door;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LockableDoorTest {

  private LockableDoor door;
  private final DoorID doorId = new DoorID("DOOR1");
  private final BuildingID buildingId = new BuildingID("BUILDING1");
  private final ZoneID zoneId = new ZoneID("ZONE1");

  @Mock private DoorState doorState;
  @Mock private DoorState initialDoorState;
  @Mock private DoorActionFactory doorActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private Action returnedAction;

  private static final boolean IS_ON_EVACUATION_PATH = true;
  private static final boolean IS_NOT_ON_EVACUATION_PATH = false;

  @BeforeEach
  void setUp() {
    door = new LockableDoor(doorId, doorState, IS_ON_EVACUATION_PATH, doorActionFactory);
  }

  @Test
  void givenDoorIsLockedAndOnEvacuationPath_whenHandleFireAlarm_thenUnlockDoorState() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);

    door.handleFireAlarm(actionContext);

    verify(doorState).setLockUnlockState(LockedUnlockedState.UNLOCKED);
  }

  @Test
  void givenDoorIsLockedAndOnEvacuationPath_whenHandleFireAlarm_thenAddUnlockAction() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(doorActionFactory.createLockUnlockDoor(
            buildingId, zoneId, doorId, LockedUnlockedState.UNLOCKED))
        .thenReturn(returnedAction);

    door.handleFireAlarm(actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenDoorIsUnlockedAndOnEvacuationPath_whenHandleFireAlarm_thenDoNotChangeDoorState() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.UNLOCKED);

    door.handleFireAlarm(actionContext);

    verify(doorState, never()).setLockUnlockState(any());
  }

  @Test
  void givenDoorIsUnlockedAndOnEvacuationPath_whenHandleFireAlarm_thenDoNotAddAnyAction() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.UNLOCKED);

    door.handleFireAlarm(actionContext);

    verify(actionContext, never()).addAction(any());
  }

  @Test
  void givenDoorIsLockedAndNotOnEvacuationPath_whenHandleFireAlarm_thenDoNotChangeDoorState() {
    door = new LockableDoor(doorId, doorState, IS_NOT_ON_EVACUATION_PATH, doorActionFactory);

    door.handleFireAlarm(actionContext);

    verify(doorState, never()).setLockUnlockState(any());
  }

  @Test
  void givenDoorIsLockedAndNotOnEvacuationPath_whenHandleFireAlarm_thenDoNotAddAnyAction() {
    door = new LockableDoor(doorId, doorState, IS_NOT_ON_EVACUATION_PATH, doorActionFactory);

    door.handleFireAlarm(actionContext);

    verify(actionContext, never()).addAction(any());
  }

  @Test
  void givenLockStateChanged_whenHandleFireAlarmCompleted_thenRevertLockState() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.UNLOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);

    door.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorState).setLockUnlockState(LockedUnlockedState.LOCKED);
  }

  @Test
  void givenLockStateChanged_whenHandleFireAlarmCompleted_thenAddLockAction() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.UNLOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(doorActionFactory.createLockUnlockDoor(
            buildingId, zoneId, doorId, LockedUnlockedState.LOCKED))
        .thenReturn(returnedAction);

    door.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenLockStateUnchanged_whenHandleFireAlarmCompleted_thenDoNotChangeDoorState() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);

    door.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorState, never()).setLockUnlockState(any());
  }

  @Test
  void givenLockStateUnchanged_whenHandleFireAlarmCompleted_thenDoNotAddAnyAction() {
    when(doorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);
    when(initialDoorState.getLockUnlockState()).thenReturn(LockedUnlockedState.LOCKED);

    door.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(actionContext, never()).addAction(any());
  }

  @Test
  void whenHandleProtest_thenLockDoorState() {
    door.handleProtest(actionContext);

    verify(doorState).setLockUnlockState(LockedUnlockedState.LOCKED);
  }

  @Test
  void whenHandleProtest_thenAddLockAction() {
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(doorActionFactory.createLockUnlockDoor(
            buildingId, zoneId, doorId, LockedUnlockedState.LOCKED))
        .thenReturn(returnedAction);

    door.handleProtest(actionContext);

    verify(actionContext).addAction(returnedAction);
  }
}
