package ca.ulaval.glo4002.application.domain.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentDispatcherTest {

  private static final String ZONE_ID = "PLT-001";
  private static final String INTERVENTION_ID = "AGENT-001";

  @Mock private AgentActionFactory agentActionFactory;
  @Mock private InterventionFactory interventionFactory;
  @Mock private ActionContext actionContext;
  @Mock private Intervention intervention;

  private AgentDispatcher dispatcher;
  private ZoneID zoneId;
  private InterventionID interventionId;
  private RequestAgentAction returnedAction;

  @BeforeEach
  void setUp() {
    dispatcher = new AgentDispatcher(agentActionFactory, interventionFactory);
    zoneId = new ZoneID(ZONE_ID);
    interventionId = new InterventionID(INTERVENTION_ID);
    returnedAction =
        new RequestAgentAction(AgentType.PATROL, null, zoneId, interventionId, AgentPriority.P3);
  }

  @Test
  void givenMultipleAgents_whenSendPatrolAgents_thenCreateMultipleAgentsAction() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 3, InterventionReason.GATHERING))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    dispatcher.sendPatrolAgents(3, AgentPriority.P3, InterventionReason.GATHERING, actionContext);

    verify(agentActionFactory, times(3))
        .createRequestAgent(AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3);
  }

  @Test
  void givenMultipleAgents_whenSendPatrolAgents_thenAddMultipleAgentActionsToContext() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 7, InterventionReason.INTRUSION))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    dispatcher.sendPatrolAgents(7, AgentPriority.P3, InterventionReason.INTRUSION, actionContext);

    verify(actionContext, times(7)).addAction(returnedAction);
  }

  @Test
  void givenMultipleAgents_whenSendPatrolAgents_thenReturnIntervention() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 2, InterventionReason.PREALARM))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    Intervention returned =
        dispatcher.sendPatrolAgents(
            2, AgentPriority.P3, InterventionReason.PREALARM, actionContext);

    assertEquals(intervention, returned);
  }

  @Test
  void
      givenLargeGroup_whenSendPatrolAgentsLargeGroupGathering_thenAgentsSentIsPeopleDividedByTwelve() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 4, InterventionReason.GATHERING))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    int people = 50;
    dispatcher.sendPatrolAgentsLargeGroupGathering(
        people, AgentPriority.P3, InterventionReason.GATHERING, actionContext);

    verify(agentActionFactory, times(people / 12))
        .createRequestAgent(AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3);
  }

  @Test
  void givenNormalGathering_whenSendPatrolAgentsNormalGathering_thenSendMinimumAmountOfAgents() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 5, InterventionReason.GATHERING))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    dispatcher.sendPatrolAgentsNormalGathering(
        20, AgentPriority.P3, InterventionReason.GATHERING, actionContext);

    verify(agentActionFactory, times(5))
        .createRequestAgent(AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3);
  }

  @Test
  void
      givenNormalGatheringWithMoreThanMinimumAgentsRequired_whenSendPatrolAgentsNormalGathering_thenSendCorrectAmountOfAgents() {
    when(interventionFactory.createIntervention(AgentPriority.P3, 6, InterventionReason.GATHERING))
        .thenReturn(intervention);
    when(intervention.getId()).thenReturn(interventionId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(agentActionFactory.createRequestAgent(
            AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3))
        .thenReturn(returnedAction);

    dispatcher.sendPatrolAgentsNormalGathering(
        67, AgentPriority.P3, InterventionReason.GATHERING, actionContext);

    verify(agentActionFactory, times(6))
        .createRequestAgent(AgentType.PATROL, null, interventionId, zoneId, AgentPriority.P3);
  }
}
