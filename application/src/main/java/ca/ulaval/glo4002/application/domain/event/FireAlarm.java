package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class FireAlarm extends Event {
  public FireAlarm(DateTime time, ZoneID zone) {
    super(time, zone);
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleFireAlarm(eventContext);
  }

  @Override
  public boolean requiresInitialCapture() {
    return true;
  }
}
