package ca.ulaval.glo4002.application.domain.action;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentActionFactoryTest {
  private AgentActionFactory agentActionFactory;

  @Mock private ZoneID zoneId;
  @Mock private InterventionID interventionId;

  private static final String AGENT_ID = "agent-1";

  @BeforeEach
  void setUp() {
    agentActionFactory = new AgentActionFactory();
  }

  @Test
  void givenAgentType_whenCreateRequestAgent_thenAgentTypeIsSet() {
    Action result =
        agentActionFactory.createRequestAgent(
            AgentType.PATROL, AGENT_ID, interventionId, zoneId, AgentPriority.P1);
    RequestAgentAction returnedAction = (RequestAgentAction) result;
    assertSame(AgentType.PATROL, returnedAction.getAgentType());
  }

  @Test
  void givenAgentId_whenCreateRequestAgent_thenAgentIdIsSet() {
    AgentID returnedAgentId = new AgentID(AGENT_ID);
    Action result =
        agentActionFactory.createRequestAgent(
            AgentType.PATROL, AGENT_ID, interventionId, zoneId, AgentPriority.P1);
    RequestAgentAction returnedAction = (RequestAgentAction) result;
    assertEquals(returnedAgentId.value(), returnedAction.getAgentId().value());
  }

  @Test
  void givenZone_whenCreateRequestAgent_thenZoneIsSet() {
    Action result =
        agentActionFactory.createRequestAgent(
            AgentType.PATROL, AGENT_ID, interventionId, zoneId, AgentPriority.P1);
    RequestAgentAction returnedAction = (RequestAgentAction) result;
    assertSame(zoneId, returnedAction.getZone());
  }

  @Test
  void givenPriority_whenCreateRequestAgent_thenPriorityIsSet() {
    Action result =
        agentActionFactory.createRequestAgent(
            AgentType.PATROL, AGENT_ID, interventionId, zoneId, AgentPriority.P2);
    RequestAgentAction returnedAction = (RequestAgentAction) result;
    assertSame(AgentPriority.P2, returnedAction.getPriority());
  }
}
