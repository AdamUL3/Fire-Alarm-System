package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.ArrayList;
import java.util.List;

public class ActionContext {
  private final BuildingID buildingID;
  private final ZoneID zoneID;
  private final List<Action> actions = new ArrayList<>();

  public ActionContext(BuildingID buildingID, ZoneID zoneID) {
    this.buildingID = buildingID;
    this.zoneID = zoneID;
  }

  public ZoneID getZoneID() {
    return zoneID;
  }

  public BuildingID getBuildingID() {
    return buildingID;
  }

  public List<Action> getActions() {
    return actions;
  }

  public void addAction(Action action) {
    actions.add(action);
  }
}
