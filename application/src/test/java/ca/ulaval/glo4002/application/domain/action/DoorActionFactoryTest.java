package ca.ulaval.glo4002.application.domain.action;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DoorActionFactoryTest {
  private DoorActionFactory doorActionFactory;

  @Mock private BuildingID buildingId;
  @Mock private ZoneID zoneId;
  @Mock private DoorID doorId;

  @BeforeEach
  void setUp() {
    doorActionFactory = new DoorActionFactory();
  }

  @Test
  void givenLockState_whenCreateLockUnlockDoor_thenLockStateIsSet() {
    Action result =
        doorActionFactory.createLockUnlockDoor(
            buildingId, zoneId, doorId, LockedUnlockedState.LOCKED);
    LockUnlockDoorAction returnedAction = (LockUnlockDoorAction) result;
    assertSame(LockedUnlockedState.LOCKED, returnedAction.getLockState());
  }

  @Test
  void givenState_whenCreateOpenCloseDoor_thenStateIsSet() {
    Action result =
        doorActionFactory.createOpenCloseDoor(buildingId, zoneId, doorId, OpenClosedState.OPEN);
    OpenCloseDoorAction returnedAction = (OpenCloseDoorAction) result;
    assertSame(OpenClosedState.OPEN, returnedAction.getOpenState());
  }
}
