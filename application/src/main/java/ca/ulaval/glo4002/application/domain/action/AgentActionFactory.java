package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;

public class AgentActionFactory {

  public Action createRequestAgent(
      AgentType agentType,
      String agentId,
      InterventionID interventionID,
      ZoneID zone,
      AgentPriority priority) {
    return new RequestAgentAction(agentType, new AgentID(agentId), zone, interventionID, priority);
  }
}
