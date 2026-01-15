package ca.ulaval.glo4002.application.domain.zone.intervention;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterventionFactoryTest {

  private InterventionFactory interventionFactory;

  @BeforeEach
  void setUp() {
    interventionFactory = new InterventionFactory();
  }

  @Test
  void
      givenPriorityP1AndAgentCount3AndReasonPrealarm_whenCreateIntervention_thenInterventionHasCorrectPriority() {
    Intervention intervention =
        interventionFactory.createIntervention(AgentPriority.P1, 3, InterventionReason.PREALARM);

    assertEquals(AgentPriority.P1, intervention.getPriority());
  }

  @Test
  void
      givenPriorityP2AndAgentCount2AndReasonIntrusion_whenCreateIntervention_thenInterventionHasCorrectReason() {
    Intervention intervention =
        interventionFactory.createIntervention(AgentPriority.P2, 2, InterventionReason.INTRUSION);

    assertEquals(InterventionReason.INTRUSION, intervention.getReason());
  }

  @Test
  void
      givenAgentCount5AndPriorityP2AndReasonIntrusion_whenCreateIntervention_thenInterventionHasCorrectAgentCount() {
    Intervention intervention =
        interventionFactory.createIntervention(AgentPriority.P2, 5, InterventionReason.INTRUSION);

    assertEquals(5, intervention.getAgentCount());
  }

  @Test
  void givenTwoInterventionsCreated_whenCreateIntervention_thenInterventionIdsAreUnique() {
    Intervention firstIntervention =
        interventionFactory.createIntervention(AgentPriority.P4, 4, InterventionReason.GATHERING);
    Intervention secondIntervention =
        interventionFactory.createIntervention(AgentPriority.P4, 4, InterventionReason.GATHERING);

    assertNotEquals(firstIntervention.getId(), secondIntervention.getId());
  }
}
