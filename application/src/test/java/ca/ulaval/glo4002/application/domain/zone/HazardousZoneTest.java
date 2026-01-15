package ca.ulaval.glo4002.application.domain.zone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.door.Door;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import ca.ulaval.glo4002.application.domain.ventilation.*;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HazardousZoneTest {

  private HazardousZone hazardousZone;

  @Mock private Door door;
  @Mock private Room room1;
  @Mock private Room room2;
  @Mock private RoomState roomState1;
  @Mock private RoomState roomState2;
  @Mock private ZoneState zoneState;
  @Mock private FireState fireState;
  @Mock private AgentDispatcher agentDispatcher;
  @Mock private AlarmActionFactory alarmActionFactory;
  @Mock private GatheringFactory gatheringFactory;
  @Mock private VentilationContextFactory ventilationContextFactory;
  @Mock private Ventilation ventilation;
  @Mock private VentilationContext ventilationContext;
  @Mock private ActionContext actionContext;
  @Mock private Intervention intervention;

  @BeforeEach
  void setUp() {
    ZoneID zoneId = new ZoneID("Z1");
    hazardousZone =
        new HazardousZone(
            zoneId,
            List.of(door),
            List.of(room1, room2),
            zoneState,
            agentDispatcher,
            alarmActionFactory,
            gatheringFactory,
            ventilationContextFactory);
  }

  @Test
  void
      givenOccupiedZoneWithEmergency_whenHandlePreAlarm_thenVentilationAdjustedWithOccupiedContext() {
    when(room1.getState()).thenReturn(roomState1);
    when(roomState1.getCurrentOccupancy()).thenReturn(5);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInHazardousZone(true))
        .thenReturn(ventilationContext);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilationContextFactory).createProbableEmergencyInHazardousZone(true);
  }

  @Test
  void givenOccupiedZoneWithEmergency_whenHandlePreAlarm_thenVentilationAdjusted() {
    when(room1.getState()).thenReturn(roomState1);
    when(roomState1.getCurrentOccupancy()).thenReturn(5);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInHazardousZone(true))
        .thenReturn(ventilationContext);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenOccupiedZoneWithEmergency_whenHandlePreAlarm_thenPatrolAgentsSent() {
    when(room1.getState()).thenReturn(roomState1);
    when(roomState1.getCurrentOccupancy()).thenReturn(5);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.PREALARM, actionContext))
        .thenReturn(intervention);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(agentDispatcher)
        .sendPatrolAgents(1, AgentPriority.P1, InterventionReason.PREALARM, actionContext);
  }

  @Test
  void givenOccupiedZoneWithEmergency_whenHandlePreAlarm_thenInterventionAddedToZoneState() {
    when(room1.getState()).thenReturn(roomState1);
    when(roomState1.getCurrentOccupancy()).thenReturn(5);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.PREALARM, actionContext))
        .thenReturn(intervention);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(zoneState).addIntervention(intervention);
  }

  @Test
  void
      givenUnoccupiedZoneWithEmergency_whenHandlePreAlarm_thenVentilationAdjustedWithUnoccupiedContext() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInUnoccupiedHazardousZone(true, 1))
        .thenReturn(ventilationContext);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilationContextFactory).createProbableEmergencyInUnoccupiedHazardousZone(true, 1);
  }

  @Test
  void givenUnoccupiedZoneWithEmergency_whenHandlePreAlarm_thenVentilationAdjusted() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInUnoccupiedHazardousZone(true, 1))
        .thenReturn(ventilationContext);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenUnoccupiedZoneWithEmergency_whenHandlePreAlarm_thenPatrolAgentsSent() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.PREALARM, actionContext))
        .thenReturn(intervention);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(agentDispatcher)
        .sendPatrolAgents(1, AgentPriority.P1, InterventionReason.PREALARM, actionContext);
  }

  @Test
  void
      givenUnoccupiedZoneWithoutEmergency_whenHandlePreAlarm_thenVentilationAdjustedWithUnoccupiedContext() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);
    when(ventilationContextFactory.createProbableEmergencyInUnoccupiedHazardousZone(false, 1))
        .thenReturn(ventilationContext);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilationContextFactory).createProbableEmergencyInUnoccupiedHazardousZone(false, 1);
  }

  @Test
  void givenUnoccupiedZoneWithoutEmergency_whenHandlePreAlarm_thenVentilationAdjusted() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(ventilationContextFactory.createProbableEmergencyInUnoccupiedHazardousZone(false, 1))
        .thenReturn(ventilationContext);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenUnoccupiedZoneWithoutEmergency_whenHandlePreAlarm_thenNoPatrolAgentsSent() {
    when(room1.getState()).thenReturn(roomState1);
    when(room2.getState()).thenReturn(roomState2);
    when(roomState1.getCurrentOccupancy()).thenReturn(0);
    when(roomState2.getCurrentOccupancy()).thenReturn(0);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);

    hazardousZone.handlePreAlarm(ventilation, actionContext);

    verifyNoInteractions(agentDispatcher);
  }
}
