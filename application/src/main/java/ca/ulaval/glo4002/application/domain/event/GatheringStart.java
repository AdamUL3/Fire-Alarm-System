package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import java.util.List;

public class GatheringStart extends Event {
  private final GatheringID gatheringId;
  private final int expectedNumberOfPeople;
  private final GatheringType gatheringType;

  public GatheringStart(
      DateTime time,
      ZoneID zoneID,
      GatheringID gatheringId,
      int expectedNumberOfPeople,
      GatheringType gatheringType) {
    super(time, zoneID);
    this.gatheringId = gatheringId;
    this.expectedNumberOfPeople = expectedNumberOfPeople;
    this.gatheringType = gatheringType;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleGatheringStart(
        eventContext, gatheringId, expectedNumberOfPeople, gatheringType);
  }
}
