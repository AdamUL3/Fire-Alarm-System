package ca.ulaval.glo4002.application.domain.zone.intervention;

import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import java.util.UUID;

public class InterventionFactory {
  public Intervention createIntervention(
      AgentPriority priority, int agentCount, InterventionReason reason) {
    return new Intervention(createInterventionId(), priority, agentCount, reason);
  }

  private InterventionID createInterventionId() {
    return new InterventionID(UUID.randomUUID().toString());
  }
}
