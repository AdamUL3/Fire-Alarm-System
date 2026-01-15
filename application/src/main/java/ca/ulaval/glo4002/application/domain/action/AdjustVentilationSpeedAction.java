package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class AdjustVentilationSpeedAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final int distributionSpeed;
  private final int returnSpeed;

  public AdjustVentilationSpeedAction(
      BuildingID buildingId, ZoneID zoneId, int distributionSpeed, int returnSpeed) {
    this.buildingId = buildingId;
    this.zoneId = zoneId;
    this.distributionSpeed = distributionSpeed;
    this.returnSpeed = returnSpeed;
  }

  public BuildingID getBuildingId() {
    return buildingId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public int getDistributionSpeed() {
    return distributionSpeed;
  }

  public int getReturnQuantity() {
    return returnSpeed;
  }
}
