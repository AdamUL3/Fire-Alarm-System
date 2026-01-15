package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentSentInterventionTest {

  private AgentSentIntervention agentSentIntervention;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private InterventionID interventionId;
  @Mock private Action returnedAction;
  @Mock private EventContext eventContext;

  @BeforeEach
  void setUp() {
    agentSentIntervention = new AgentSentIntervention(validTime, zoneId, interventionId);
  }

  @Test
  void givenBuildingAndInterventionId_whenHandleEvent_thenReturnAgentSentActions() {
    List<Action> expectedActions = List.of(returnedAction);
    when(building.handleAgentSent(eventContext, interventionId)).thenReturn(expectedActions);

    List<Action> result = agentSentIntervention.handleEvent(building, eventContext);

    assertEquals(expectedActions, result);
  }
}
