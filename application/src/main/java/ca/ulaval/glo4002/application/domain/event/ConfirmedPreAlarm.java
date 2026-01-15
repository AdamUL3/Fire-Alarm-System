package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class ConfirmedPreAlarm extends Event {
  public ConfirmedPreAlarm(DateTime time, ZoneID zone) {
    super(time, zone);
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleFireAlarm(eventContext);
  }
}
