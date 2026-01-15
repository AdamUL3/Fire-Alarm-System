package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class FireAlarmCompleted extends Event {
  public FireAlarmCompleted(DateTime time, ZoneID zone) {
    super(time, zone);
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleFireAlarmCompleted(eventContext);
  }

  @Override
  public boolean endsIncident() {
    return true;
  }
}
