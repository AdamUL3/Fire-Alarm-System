package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class VentilationActionFactory {
  public Action createAdjustVentilationSpeed(
      BuildingID building, ZoneID zone, int distributionSpeed, int returnQuantity) {
    return new AdjustVentilationSpeedAction(building, zone, distributionSpeed, returnQuantity);
  }

  public Action createOpenCloseVentilation(
      BuildingID building, ZoneID zone, OpenClosedState state) {
    return new OpenCloseVentilationAction(building, zone, state);
  }
}
