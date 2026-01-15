package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public abstract class Event {
  protected DateTime time;
  protected ZoneID activeZoneID;

  public Event(DateTime time, ZoneID zoneID) {
    this.time = time;
    this.activeZoneID = zoneID;
  }

  public ZoneID getZone() {
    return activeZoneID;
  }

  public abstract List<Action> handleEvent(Building building, EventContext eventContext);

  public boolean requiresInitialCapture() {
    return false;
  }

  public boolean endsIncident() {
    return false;
  }
}
