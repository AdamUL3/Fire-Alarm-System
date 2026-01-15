package ca.ulaval.glo4002.application.domain.building;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.access.CardID;
import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.event.EventContext;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.zone.AggregateZoneState;
import ca.ulaval.glo4002.application.domain.zone.FireState;
import ca.ulaval.glo4002.application.domain.zone.Zone;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuildingTest {

  @Mock private Ventilation ventilation;
  @Mock private Zone zone1;
  @Mock private Zone zone2;
  @Mock private FirefighterActionFactory firefighterActionFactory;
  @Mock private ActionContextFactory actionContextFactory;
  @Mock private EventContext eventContext;
  @Mock private AggregateBuildingState initialBuildingState;
  @Mock private AggregateZoneState initialZoneState;
  @Mock private Action action1;
  @Mock private Action action2;
  @Mock private ActionContext actionContext1;
  @Mock private ActionContext actionContext2;

  private Building building;
  private ZoneID zone1Id;
  private ZoneID zone2Id;
  private RoomID roomId;
  private GatheringID gatheringId;
  private GatheringType gatheringType;
  private InterventionID interventionId;
  private DoorID doorId;
  private PersonID personId;
  private CardID cardId;
  private BuildingID buildingId;
  private BuildingAddress buildingAddress;

  @BeforeEach
  void setUp() {
    buildingId = new BuildingID("B1");
    buildingAddress = new BuildingAddress("123 Street");
    zone1Id = new ZoneID("zone1");
    zone2Id = new ZoneID("zone2");
    roomId = new RoomID("PLT");
    gatheringId = new GatheringID("GATHERINT");
    gatheringType = GatheringType.ACADEMIC;
    interventionId = new InterventionID("INTERVENTION");
    doorId = new DoorID("DOOR123");
    personId = new PersonID("PERSON789");
    cardId = new CardID("CARD123");

    lenient().when(zone1.getId()).thenReturn(zone1Id);
    lenient().when(zone2.getId()).thenReturn(zone2Id);
    lenient()
        .when(actionContextFactory.createActionContext(buildingId, zone1Id))
        .thenReturn(actionContext1);
    lenient()
        .when(actionContextFactory.createActionContext(buildingId, zone2Id))
        .thenReturn(actionContext2);

    building =
        new Building(
            buildingId,
            buildingAddress,
            List.of(zone1, zone2),
            ventilation,
            firefighterActionFactory,
            actionContextFactory);
  }

  @Test
  void givenEventWithZone1_whenHandlePreAlarm_thenReturnsActionFromZone1() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);
    when(actionContext1.getActions()).thenReturn(List.of(action1));

    List<Action> actions = building.handlePreAlarm(eventContext);

    assertTrue(actions.contains(action1));
  }

  @Test
  void givenEventWithZone2_whenHandlePreAlarm_thenReturnsActionFromZone2() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);
    when(actionContext2.getActions()).thenReturn(List.of(action2));

    List<Action> actions = building.handlePreAlarm(eventContext);

    assertTrue(actions.contains(action2));
  }

  @Test
  void givenZoneWithLargeGroup_whenHandlePreAlarm_thenFireAlarmCalledOnZone() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);
    when(zone2.hasLargeGroup()).thenReturn(true);

    building.handlePreAlarm(eventContext);

    verify(zone2).handleFireAlarm(ventilation, actionContext2);
  }

  @Test
  void givenZones_whenHandleCancelledPreAlarm_thenZone1CancelledPreAlarmCalled() {
    when(eventContext.initialBuildingState()).thenReturn(Optional.of(initialBuildingState));
    Map<ZoneID, AggregateZoneState> zoneStates = new HashMap<>();
    zoneStates.put(zone1Id, initialZoneState);
    when(initialBuildingState.zoneStates()).thenReturn(zoneStates);

    building.handleCancelledPreAlarm(eventContext);

    verify(zone1).handleCancelledPreAlarm(ventilation, actionContext1, initialZoneState);
  }

  @Test
  void givenZones_whenHandleFireAlarmCompleted_thenZone1FireAlarmCompletedCalled() {
    when(eventContext.initialBuildingState()).thenReturn(Optional.of(initialBuildingState));
    Map<ZoneID, AggregateZoneState> zoneStates = new HashMap<>();
    zoneStates.put(zone1Id, initialZoneState);
    when(initialBuildingState.zoneStates()).thenReturn(zoneStates);

    building.handleFireAlarmCompleted(eventContext);

    verify(zone1).handleFireAlarmCompleted(ventilation, actionContext1, initialZoneState);
  }

  @Test
  void givenSmokeBelowThreshold_whenHandleSmokePresence_thenZone1PreAlarmCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleSmokePresence(19, eventContext);

    verify(zone1).handlePreAlarm(ventilation, actionContext1);
  }

  @Test
  void givenSmokeBelowThreshold_whenHandleSmokePresence_thenZone1FireStateSetProbable() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleSmokePresence(19, eventContext);

    verify(zone1).setFireState(FireState.PROBABLE);
  }

  @Test
  void givenSmokeAboveThreshold_whenHandleSmokePresence_thenZone2FireAlarmCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);

    building.handleSmokePresence(21, eventContext);

    verify(zone2).handleFireAlarm(ventilation, actionContext2);
  }

  @Test
  void givenZones_whenHandleFireAlarm_thenZone1FireAlarmCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleFireAlarm(eventContext);

    verify(zone1).handleFireAlarm(ventilation, actionContext1);
  }

  @Test
  void givenZones_whenHandleFireAlarm_thenFirefighterActionCreated() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleFireAlarm(eventContext);

    verify(firefighterActionFactory).createCallFirefighters(buildingAddress, CallReasonType.FIRE);
  }

  @Test
  void givenZone2_whenHandleNoCardEntry_thenZone2HandleNoCardEntryCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);

    building.handleNoCardEntry(eventContext, roomId);

    verify(zone2).handleNoCardEntry(roomId, actionContext2);
  }

  @Test
  void givenZone1_whenHandleExit_thenZone1HandleExitCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleExit(eventContext, roomId);

    verify(zone1).handleExitRoom(roomId);
  }

  @Test
  void givenZone1_whenHandleGatheringStart_thenZone1HandleGatheringStartCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleGatheringStart(eventContext, gatheringId, 10, gatheringType);

    verify(zone1).handleGatheringStart(actionContext1, gatheringId, 10, gatheringType);
  }

  @Test
  void givenZone2_whenHandleGatheringEnd_thenZone2HandleGatheringEndCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);

    building.handleGatheringEnd(eventContext, gatheringId);

    verify(zone2).handleGatheringEnd(gatheringId);
  }

  @Test
  void givenZone1_whenHandleAgentArrived_thenZone1HandleAgentArrivedCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleAgentArrived(eventContext, interventionId);

    verify(zone1).handleAgentArrived(interventionId, actionContext1);
  }

  @Test
  void givenZone2_whenHandleAgentLeft_thenZone2HandleAgentLeftCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone2Id);

    building.handleAgentLeft(eventContext, interventionId);

    verify(zone2).handleAgentLeft(interventionId);
  }

  @Test
  void givenZone1_whenHandleAgentSent_thenZone1HandleAgentSentCalled() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);

    building.handleAgentSent(eventContext, interventionId);

    verify(zone1).handleAgentSent(interventionId);
  }

  @Test
  void givenNullCard_whenHandleAccess_thenThrowsRefusedAccess() {
    PersonAccessInformation personAccessInfo =
        new PersonAccessInformation(null, personId, AccessRole.REGULAR, false);

    assertThrows(
        RefusedAccessException.class,
        () -> building.handleAccess(zone1Id, roomId, doorId, personAccessInfo));
  }

  @Test
  void givenValidCardAndNotLabResponsible_whenHandleAccess_thenZone1HandleAccessCalled() {
    PersonAccessInformation personAccessInfo =
        new PersonAccessInformation(cardId, personId, AccessRole.REGULAR, false);

    when(zone1.isLabResponsibleInZone(personId)).thenReturn(false);

    building.handleAccess(zone1Id, roomId, doorId, personAccessInfo);

    verify(zone1).handleAccess(roomId, doorId, personAccessInfo);
  }

  @Test
  void givenPersonLabResponsible_whenHandleAccess_thenLabResponsibleInBuildingSetTrue() {
    PersonAccessInformation personAccessInfo =
        new PersonAccessInformation(cardId, personId, AccessRole.REGULAR, false);

    when(zone2.isLabResponsibleInZone(personId)).thenReturn(true);

    building.handleAccess(zone1Id, roomId, doorId, personAccessInfo);

    assertTrue(personAccessInfo.isLabResponsibleInBuilding());
  }

  @Test
  void givenUnknownZone_whenHandleAccess_thenThrowsRuntimeException() {
    ZoneID unknownZoneId = new ZoneID("UNKNOWN");
    PersonAccessInformation personAccessInfo =
        new PersonAccessInformation(cardId, personId, AccessRole.REGULAR, false);

    assertThrows(
        RuntimeException.class,
        () -> building.handleAccess(unknownZoneId, roomId, doorId, personAccessInfo));
  }

  @Test
  void givenEventInZone_whenHandleCancelledPreAlarm_thenFireStateSetNone() {
    when(eventContext.activeZoneId()).thenReturn(zone1Id);
    when(eventContext.initialBuildingState()).thenReturn(Optional.of(initialBuildingState));
    Map<ZoneID, AggregateZoneState> zoneStates = new HashMap<>();
    zoneStates.put(zone1Id, initialZoneState);
    when(initialBuildingState.zoneStates()).thenReturn(zoneStates);

    building.handleCancelledPreAlarm(eventContext);

    verify(zone1).setFireState(FireState.NONE);
  }
}
