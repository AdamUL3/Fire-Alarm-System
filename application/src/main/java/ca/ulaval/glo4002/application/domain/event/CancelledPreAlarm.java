package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class CancelledPreAlarm extends Event {
  public CancelledPreAlarm(DateTime time, ZoneID zone) {
    super(time, zone);
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleCancelledPreAlarm(eventContext);
  }

  @Override
  public boolean endsIncident() {
    return true;
  }
}
