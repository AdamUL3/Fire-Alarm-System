package ca.ulaval.glo4002.application.domain.zone;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.AgentActionFactory;
import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.AgentType;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;

public class AgentDispatcher {
  private final AgentActionFactory agentActionFactory;
  private final InterventionFactory interventionFactory;

  public AgentDispatcher(AgentActionFactory factory, InterventionFactory interventionFactory) {
    this.agentActionFactory = factory;
    this.interventionFactory = interventionFactory;
  }

  public Intervention sendPatrolAgents(
      int numberOfAgents,
      AgentPriority priority,
      InterventionReason reason,
      ActionContext actionContext) {
    Intervention intervention =
        interventionFactory.createIntervention(priority, numberOfAgents, reason);
    for (int i = 0; i < numberOfAgents; i++) {
      actionContext.addAction(
          agentActionFactory.createRequestAgent(
              AgentType.PATROL, null, intervention.getId(), actionContext.getZoneID(), priority));
    }
    return intervention;
  }

  public Intervention sendPatrolAgentsLargeGroupGathering(
      int expectedPeopleCount,
      AgentPriority priority,
      InterventionReason reason,
      ActionContext actionContext) {
    return sendPatrolAgents(expectedPeopleCount / 12, priority, reason, actionContext);
  }

  public Intervention sendPatrolAgentsNormalGathering(
      int expectedPeopleCount,
      AgentPriority priority,
      InterventionReason reason,
      ActionContext actionContext) {
    int agentsToSend = expectedPeopleCount / 10;
    if (agentsToSend < 5) {
      agentsToSend = 5;
    }
    return sendPatrolAgents(agentsToSend, priority, reason, actionContext);
  }
}
