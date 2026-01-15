package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class AlarmActionFactory {
  public Action createActivateFireAlarm(BuildingID building, ZoneID zone, AlarmState ringState) {
    return new ActivateFireAlarmAction(building, zone, ringState);
  }
}
