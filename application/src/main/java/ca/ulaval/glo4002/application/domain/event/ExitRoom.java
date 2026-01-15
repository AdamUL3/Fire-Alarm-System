package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class ExitRoom extends Event {

  private final RoomID roomId;

  public ExitRoom(DateTime time, ZoneID zoneID, RoomID roomId) {
    super(time, zoneID);
    this.roomId = roomId;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleExit(eventContext, roomId);
  }
}
