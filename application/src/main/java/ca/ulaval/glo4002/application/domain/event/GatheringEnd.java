package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import java.util.List;

public class GatheringEnd extends Event {
  private final GatheringID gatheringID;

  public GatheringEnd(DateTime time, ZoneID zoneID, GatheringID gatheringID) {
    super(time, zoneID);
    this.gatheringID = gatheringID;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleGatheringEnd(eventContext, gatheringID);
  }
}
