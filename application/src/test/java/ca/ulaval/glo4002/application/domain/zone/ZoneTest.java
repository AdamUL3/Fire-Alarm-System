package ca.ulaval.glo4002.application.domain.zone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.door.Door;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContext;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationState;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ZoneTest {

  private ZoneID zoneId;
  private RoomID roomId;
  private GatheringID gatheringId;
  private PersonID personId;
  private BuildingID buildingId;

  @Mock private AgentDispatcher agentDispatcher;
  @Mock private AlarmActionFactory alarmActionFactory;
  @Mock private GatheringFactory gatheringFactory;
  @Mock private Door door;
  @Mock private Room room;
  @Mock private Ventilation ventilation;
  @Mock private ActionContext actionContext;
  @Mock private VentilationState ventilationState;
  @Mock private VentilationState initialVentilationState;
  @Mock private VentilationContextFactory ventilationContextFactory;
  @Mock private VentilationContext ventilationContext;
  @Mock private ZoneState zoneState;
  @Mock private AggregateZoneState initialAggregateZoneState;
  @Mock private ZoneState initialZoneState;
  @Mock private Intervention intervention;
  @Mock private PersonAccessInformation accessInfo;
  @Mock private FireState fireState;

  private Zone zone;

  @BeforeEach
  void setUp() {
    zoneId = new ZoneID("Z1");
    roomId = new RoomID("R1");
    gatheringId = new GatheringID("G1");
    personId = new PersonID("P1");
    buildingId = new BuildingID("B1");

    zone =
        new Zone(
            zoneId,
            List.of(door),
            List.of(room),
            zoneState,
            agentDispatcher,
            alarmActionFactory,
            gatheringFactory,
            ventilationContextFactory);
  }

  @Test
  void givenZone_whenSetFireState_thenDelegateToZoneState() {
    zone.setFireState(FireState.PROBABLE);
    verify(zoneState).setFireState(FireState.PROBABLE);
  }

  @Test
  void givenZone_whenSetSmokePresence_thenDelegateToZoneState() {
    zone.setSmokePresence(true);
    verify(zoneState).setSmokePresence(true);
  }

  @Test
  void givenFireInactive_whenHandleFireAlarmCompleted_thenRevertVentilation() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);
    when(initialAggregateZoneState.zoneState()).thenReturn(initialZoneState);
    when(initialZoneState.getVentilationState()).thenReturn(initialVentilationState);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);

    zone.handleFireAlarmCompleted(ventilation, actionContext, initialAggregateZoneState);

    verify(ventilation).revert(ventilationState, initialVentilationState, actionContext);
  }

  @Test
  void givenFireActive_whenHandleFireAlarmCompleted_thenNoRevert() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);

    zone.handleFireAlarmCompleted(ventilation, actionContext, initialAggregateZoneState);
  }

  @Test
  void givenPreAlarmFireActive_whenHandlePreAlarm_thenAdjustVentilation() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInRegularZone(true))
        .thenReturn(ventilationContext);

    zone.handlePreAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenPreAlarmFireActive_whenHandlePreAlarm_thenSendPatrol() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(agentDispatcher.sendPatrolAgents(
            1, AgentPriority.P1, InterventionReason.PREALARM, actionContext))
        .thenReturn(intervention);

    zone.handlePreAlarm(ventilation, actionContext);

    verify(zoneState).addIntervention(intervention);
  }

  @Test
  void givenPreAlarmFireInactive_whenHandlePreAlarm_thenAdjustVentilationOnly() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);
    when(ventilationContextFactory.createProbableEmergencyInRegularZone(false))
        .thenReturn(ventilationContext);

    zone.handlePreAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenFireInactive_whenHandleCancelledPreAlarm_thenRevertVentilation() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);
    when(initialAggregateZoneState.zoneState()).thenReturn(initialZoneState);
    when(initialZoneState.getVentilationState()).thenReturn(initialVentilationState);

    zone.handleCancelledPreAlarm(ventilation, actionContext, initialAggregateZoneState);

    verify(ventilation)
        .revert(zoneState.getVentilationState(), initialVentilationState, actionContext);
  }

  @Test
  void givenFireActive_whenHandleCancelledPreAlarm_thenDoNothing() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);

    zone.handleCancelledPreAlarm(ventilation, actionContext, initialAggregateZoneState);
  }

  @Test
  void givenFireActive_whenHandleFireAlarm_thenActivateAlarm() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(actionContext.getBuildingID()).thenReturn(buildingId);

    zone.handleFireAlarm(ventilation, actionContext);

    verify(alarmActionFactory).createActivateFireAlarm(buildingId, zoneId, AlarmState.RING);
  }

  @Test
  void givenFireActive_whenHandleFireAlarm_thenAdjustVentilation() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(ventilationContextFactory.createProbableEmergencyInRegularZone(true))
        .thenReturn(ventilationContext);

    zone.handleFireAlarm(ventilation, actionContext);

    verify(ventilation).adjust(zoneState.getVentilationState(), ventilationContext, actionContext);
  }

  @Test
  void givenFireActive_whenHandleFireAlarm_thenNotifyDoorAndRoom() {
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);

    zone.handleFireAlarm(ventilation, actionContext);

    verify(door).handleFireAlarm(actionContext);
    verify(room).handleFireAlarm(actionContext);
  }

  @Test
  void givenNoCardEntry_whenHandleNoCardEntry_thenAddIntervention() {
    when(room.handleNoCardEntry(actionContext)).thenReturn(intervention);
    when(room.getId()).thenReturn(roomId);

    zone.handleNoCardEntry(roomId, actionContext);

    verify(zoneState).addIntervention(intervention);
  }

  @Test
  void givenNoIntervention_whenHandleNoCardEntry_thenNothingAdded() {
    when(room.handleNoCardEntry(actionContext)).thenReturn(null);
    when(room.getId()).thenReturn(roomId);

    zone.handleNoCardEntry(roomId, actionContext);

    verify(zoneState, never()).addIntervention(any());
  }

  @Test
  void givenGatheringAcademic_whenHandleGatheringStart_thenAddGatheringOnly() {
    Gathering gathering = mock(Gathering.class);
    when(gatheringFactory.createGathering(gatheringId, 10, GatheringType.ACADEMIC))
        .thenReturn(gathering);
    when(zoneState.hasLargeGroup()).thenReturn(false);

    zone.handleGatheringStart(actionContext, gatheringId, 10, GatheringType.ACADEMIC);

    verify(zoneState).addGathering(gathering);
  }

  @Test
  void givenGatheringProtest_whenHandleGatheringStart_thenSendIntervention() {
    Gathering gathering = mock(Gathering.class);
    when(gatheringFactory.createGathering(gatheringId, 20, GatheringType.PROTEST))
        .thenReturn(gathering);

    zone.handleGatheringStart(actionContext, gatheringId, 20, GatheringType.PROTEST);

    verify(agentDispatcher)
        .sendPatrolAgents(2, AgentPriority.P2, InterventionReason.GATHERING, actionContext);
  }

  @Test
  void givenGatheringOtherRisk_whenHandleGatheringStart_thenSendIntervention() {
    Gathering gathering = mock(Gathering.class);
    when(gatheringFactory.createGathering(gatheringId, 15, GatheringType.OTHER_RISK))
        .thenReturn(gathering);

    zone.handleGatheringStart(actionContext, gatheringId, 15, GatheringType.OTHER_RISK);

    verify(agentDispatcher)
        .sendPatrolAgents(1, AgentPriority.P2, InterventionReason.GATHERING, actionContext);
  }

  @Test
  void givenGatheringWithLargeGroup_whenHandleGatheringStart_thenSendInterventionP3() {
    Gathering gathering = mock(Gathering.class);
    when(gatheringFactory.createGathering(gatheringId, 60, GatheringType.ACADEMIC))
        .thenReturn(gathering);
    when(zoneState.hasLargeGroup()).thenReturn(true);

    zone.handleGatheringStart(actionContext, gatheringId, 60, GatheringType.ACADEMIC);

    verify(agentDispatcher)
        .sendPatrolAgents(1, AgentPriority.P3, InterventionReason.GATHERING, actionContext);
  }

  @Test
  void givenFireActiveAndPersonIsSecurity_whenHandleAccess_thenNoException() {
    when(accessInfo.getRole()).thenReturn(AccessRole.SECURITY_AGENT);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(false);

    assertDoesNotThrow(() -> zone.handleAccess(null, null, accessInfo));
  }

  @Test
  void givenExtremePressureAndRoomNull_whenHandleAccess_thenThrow() {
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(true);

    assertThrows(RefusedAccessException.class, () -> zone.handleAccess(null, null, accessInfo));
  }

  @Test
  void givenRoomExists_whenHandleAccess_thenAddOccupant() {
    when(accessInfo.getPersonId()).thenReturn(personId);
    when(room.getId()).thenReturn(roomId);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(false);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(false);

    zone.handleAccess(roomId, null, accessInfo);

    verify(room).addOccupant(personId);
  }

  @Test
  void givenFireActiveAndRoomExists_whenHandleAccess_thenHandleAccessDuringEmergency() {
    when(accessInfo.getRole()).thenReturn(AccessRole.REGULAR);
    when(accessInfo.getPersonId()).thenReturn(personId);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(false);
    when(room.getId()).thenReturn(roomId);

    zone.handleAccess(roomId, null, accessInfo);

    verify(room).handleAccessDuringEmergency(accessInfo);
  }

  @Test
  void givenFireActiveAndLabResponsible_whenHandleAccess_thenNoException() {
    when(accessInfo.getRole()).thenReturn(AccessRole.REGULAR);
    when(accessInfo.getPersonId()).thenReturn(personId);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(room.isLabResponsible(personId)).thenReturn(true);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(false);

    assertDoesNotThrow(() -> zone.handleAccess(null, null, accessInfo));
  }

  @Test
  void givenFireActiveAndNoRoomAndNotLabResponsible_whenHandleAccess_thenThrow() {
    when(accessInfo.getRole()).thenReturn(AccessRole.REGULAR);
    when(accessInfo.getPersonId()).thenReturn(personId);
    when(zoneState.getFireState()).thenReturn(fireState);
    when(fireState.hasEmergency()).thenReturn(true);
    when(room.isLabResponsible(personId)).thenReturn(false);
    when(zoneState.getVentilationState()).thenReturn(ventilationState);
    when(ventilationState.hasExtremePressure()).thenReturn(false);

    assertThrows(RefusedAccessException.class, () -> zone.handleAccess(null, null, accessInfo));
  }

  @Test
  void givenLabResponsible_whenIsLabResponsibleInZone_thenReturnTrue() {
    when(room.isLabResponsible(personId)).thenReturn(true);
    assertTrue(zone.isLabResponsibleInZone(personId));
  }

  @Test
  void givenNotLabResponsible_whenIsLabResponsibleInZone_thenReturnFalse() {
    when(room.isLabResponsible(personId)).thenReturn(false);
    assertFalse(zone.isLabResponsibleInZone(personId));
  }
}
