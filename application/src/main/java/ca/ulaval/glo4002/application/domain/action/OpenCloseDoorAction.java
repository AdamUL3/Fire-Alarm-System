package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class OpenCloseDoorAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final DoorID doorId;
  private final OpenClosedState doorState;

  public OpenCloseDoorAction(
      BuildingID buildingId, ZoneID zoneId, DoorID doorId, OpenClosedState doorState) {
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

  public OpenClosedState getOpenState() {
    return doorState;
  }
}
