package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class ActivateFireAlarmAction extends Action {
  private final BuildingID buildingId;
  private final ZoneID zoneId;
  private final AlarmState alarmState;

  public ActivateFireAlarmAction(BuildingID buildingId, ZoneID zoneId, AlarmState ringState) {
    this.buildingId = buildingId;
    this.zoneId = zoneId;
    this.alarmState = ringState;
  }

  public BuildingID getBuildingId() {
    return buildingId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public AlarmState getAlarmState() {
    return alarmState;
  }
}
