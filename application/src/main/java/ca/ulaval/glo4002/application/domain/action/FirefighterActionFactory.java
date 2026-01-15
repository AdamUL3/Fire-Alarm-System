package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingAddress;

public class FirefighterActionFactory {

  public Action createCallFirefighters(BuildingAddress buildingAddress, CallReasonType reasonType) {
    return new CallFirefightersAction(buildingAddress, reasonType);
  }
}
