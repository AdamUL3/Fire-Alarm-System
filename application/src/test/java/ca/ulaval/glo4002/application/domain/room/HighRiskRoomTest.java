package ca.ulaval.glo4002.application.domain.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.AgentDispatcher;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighRiskRoomTest {

  private HighRiskRoom highRiskRoom;

  @Mock private RoomState roomState;
  @Mock private RoomActionFactory roomActionFactory;
  @Mock private AgentDispatcher agentDispatcher;
  @Mock private AlarmActionFactory alarmActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private Intervention returnedIntervention;
  @Mock private ActivateFireAlarmAction alarmAction;

  @BeforeEach
  void setUp() {
    BuildingID buildingId = new BuildingID("B1");
    ZoneID zoneId = new ZoneID("Z1");

    highRiskRoom =
        new HighRiskRoom(
            new RoomID("HR1"), roomState, roomActionFactory, agentDispatcher, alarmActionFactory);
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(alarmActionFactory.createActivateFireAlarm(buildingId, zoneId, AlarmState.RING))
        .thenReturn(alarmAction);
    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.INTRUSION, actionContext))
        .thenReturn(returnedIntervention);
  }

  @Test
  void givenActionContext_whenHandleNoCardEntry_thenAddAlarmActionToActionContext() {
    highRiskRoom.handleNoCardEntry(actionContext);

    verify(actionContext).addAction(alarmAction);
  }

  @Test
  void givenActionContext_whenHandleNoCardEntry_thenAddOccupant() {
    highRiskRoom.handleNoCardEntry(actionContext);

    verify(roomState).addOccupant(null);
  }

  @Test
  void givenActionContext_whenHandleNoCardEntry_thenReturnInterventionFromAgentDispatcher() {
    Intervention result = highRiskRoom.handleNoCardEntry(actionContext);

    assertEquals(returnedIntervention, result);
  }
}
