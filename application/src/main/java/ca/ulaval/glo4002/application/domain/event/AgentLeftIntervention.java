package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import java.util.List;

public class AgentLeftIntervention extends Event {
  private final InterventionID interventionId;

  public AgentLeftIntervention(DateTime time, ZoneID zone, InterventionID interventionId) {
    super(time, zone);
    this.interventionId = interventionId;
  }

  @Override
  public List<Action> handleEvent(Building building, EventContext eventContext) {
    return building.handleAgentLeft(eventContext, interventionId);
  }
}
