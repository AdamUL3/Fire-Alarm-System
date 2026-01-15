package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class DoorActionFactory {
  public Action createLockUnlockDoor(
      BuildingID building, ZoneID zone, DoorID door, LockedUnlockedState lockState) {
    return new LockUnlockDoorAction(building, zone, door, lockState);
  }

  public Action createOpenCloseDoor(
      BuildingID building, ZoneID zone, DoorID doorId, OpenClosedState state) {
    return new OpenCloseDoorAction(building, zone, doorId, state);
  }
}
