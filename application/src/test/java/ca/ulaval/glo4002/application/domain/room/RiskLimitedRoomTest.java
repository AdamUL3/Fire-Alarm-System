package ca.ulaval.glo4002.application.domain.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.zone.AgentDispatcher;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RiskLimitedRoomTest {

  private RiskLimitedRoom riskLimitedRoom;

  @Mock private RoomState roomState;
  @Mock private RoomActionFactory roomActionFactory;
  @Mock private AgentDispatcher agentDispatcher;
  @Mock private ActionContext actionContext;
  @Mock private Intervention returnedIntervention;

  @BeforeEach
  void setUp() {
    riskLimitedRoom =
        new RiskLimitedRoom(new RoomID("RL1"), roomState, roomActionFactory, agentDispatcher);

    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.INTRUSION, actionContext))
        .thenReturn(returnedIntervention);
  }

  @Test
  void givenActionContext_whenHandleNoCardEntry_thenAddOccupant() {
    riskLimitedRoom.handleNoCardEntry(actionContext);

    verify(roomState).addOccupant(null);
  }

  @Test
  void givenActionContext_whenHandleNoCardEntry_thenReturnInterventionFromAgentDispatcher() {
    Intervention result = riskLimitedRoom.handleNoCardEntry(actionContext);

    assertEquals(returnedIntervention, result);
  }
}
