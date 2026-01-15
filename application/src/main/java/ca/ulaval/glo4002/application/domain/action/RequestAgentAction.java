package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;

public class RequestAgentAction extends Action {
  private final AgentType agentType;
  private final AgentID agentId;
  private final ZoneID zoneId;
  private final AgentPriority priority;
  private final InterventionID interventionId;

  public RequestAgentAction(
      AgentType agentType,
      AgentID agentId,
      ZoneID zoneId,
      InterventionID interventionID,
      AgentPriority priority) {
    this.agentType = agentType;
    this.agentId = agentId;
    this.zoneId = zoneId;
    this.priority = priority;
    this.interventionId = interventionID;
  }

  public AgentType getAgentType() {
    return agentType;
  }

  public AgentID getAgentId() {
    return agentId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public AgentPriority getPriority() {
    return priority;
  }

  public InterventionID getInterventionId() {
    return interventionId;
  }

  public ZoneID getZone() {
    return zoneId;
  }
}
