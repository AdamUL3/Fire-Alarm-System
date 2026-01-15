package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingAddress;

public class CallFirefightersAction extends Action {
  private final BuildingAddress buildingAdress;
  private final CallReasonType callReason;

  public CallFirefightersAction(BuildingAddress buildingAdress, CallReasonType callReason) {
    this.buildingAdress = buildingAdress;
    this.callReason = callReason;
  }

  public BuildingAddress getBuildingAddress() {
    return buildingAdress;
  }

  public CallReasonType getCallReason() {
    return callReason;
  }
}
