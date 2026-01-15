package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class LockUnlockDoorAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final DoorID doorId;
  private final LockedUnlockedState doorState;

  public LockUnlockDoorAction(
      BuildingID buildingId, ZoneID zoneId, DoorID doorId, LockedUnlockedState doorState) {
    this.buildingId = buildingId;
    this.zoneId = zoneId;
    this.doorId = doorId;
    this.doorState = doorState;
  }

  public BuildingID getBuildingId() {
    return buildingId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public DoorID getDoorId() {
    return doorId;
  }

  public LockedUnlockedState getLockState() {
    return doorState;
  }
}
