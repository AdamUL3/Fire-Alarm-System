package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class NoCardEntry extends Event {
  private final RoomID roomId;

  public NoCardEntry(DateTime time, ZoneID zone, RoomID roomId) {
    super(time, zone);
    this.roomId = roomId;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleNoCardEntry(eventContext, roomId);
  }
}
