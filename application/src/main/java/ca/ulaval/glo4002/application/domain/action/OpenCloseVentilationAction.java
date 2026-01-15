package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class OpenCloseVentilationAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final OpenClosedState ventilationState;

  public OpenCloseVentilationAction(
      BuildingID buildingId, ZoneID zoneId, OpenClosedState ventilationState) {
    this.buildingId = buildingId;
    this.zoneId = zoneId;
    this.ventilationState = ventilationState;
  }

  public BuildingID getBuildingId() {
    return buildingId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public OpenClosedState getVentilationState() {
    return ventilationState;
  }
}
