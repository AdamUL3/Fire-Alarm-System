package ca.ulaval.glo4002.application.domain.zone.intervention;

import ca.ulaval.glo4002.application.domain.action.AgentPriority;

public class Intervention {
  private final InterventionID interventionId;
  private final AgentPriority priority;
  private final int agentCount;
  private final InterventionReason interventionReason;

  public Intervention(
      InterventionID interventionId,
      AgentPriority priority,
      int agentCount,
      InterventionReason interventionReason) {
    this.interventionId = interventionId;
    this.priority = priority;
    this.agentCount = agentCount;
    this.interventionReason = interventionReason;
  }

  public InterventionID getId() {
    return interventionId;
  }

  public AgentPriority getPriority() {
    return priority;
  }

  public InterventionReason getReason() {
    return interventionReason;
  }

  public int getAgentCount() {
    return agentCount;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Intervention i) {
      return i.interventionId.equals(this.interventionId);
    }
    return false;
  }
}
