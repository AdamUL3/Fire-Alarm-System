package ca.ulaval.glo4002.application.domain.building;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class ActionContextFactory {
  public ActionContext createActionContext(BuildingID buildingId, ZoneID zoneId) {
    return new ActionContext(buildingId, zoneId);
  }
}
