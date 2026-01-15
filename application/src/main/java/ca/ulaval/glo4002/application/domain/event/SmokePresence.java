package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;

public class SmokePresence extends Event {
  private final int smokeConcentration;

  public SmokePresence(DateTime time, ZoneID zone, int smokeConcentration) {
    super(time, zone);
    this.smokeConcentration = smokeConcentration;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleSmokePresence(smokeConcentration, eventContext);
  }

  @Override
  public boolean requiresInitialCapture() {
    return true;
  }
}
