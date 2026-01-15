package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class FirePreAlarm extends Event {
  public FirePreAlarm(DateTime time, ZoneID zone) {
    super(time, zone);
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handlePreAlarm(eventContext);
  }

  @Override
  public boolean requiresInitialCapture() {
    return true;
  }
}
