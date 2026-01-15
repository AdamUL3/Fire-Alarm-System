package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class OpenClosePowerAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final RoomID roomId;
  private final OpenClosedState electricityState;

  public OpenClosePowerAction(
      BuildingID buildingId, ZoneID zoneId, RoomID roomId, OpenClosedState electricityState) {
    this.buildingId = buildingId;
    this.zoneId = zoneId;
    this.roomId = roomId;
    this.electricityState = electricityState;
  }

  public BuildingID getBuildingId() {
    return buildingId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public RoomID getRoomId() {
    return roomId;
  }

  public OpenClosedState getElectricityState() {
    return electricityState;
  }
}
