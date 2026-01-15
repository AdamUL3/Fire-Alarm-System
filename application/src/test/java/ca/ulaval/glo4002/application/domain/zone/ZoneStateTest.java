package ca.ulaval.glo4002.application.domain.zone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.ventilation.VentilationState;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringNotFoundException;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ZoneStateTest {

  private ZoneState zoneState;
  private List<Gathering> gatherings;
  private List<Intervention> interventions;

  @Mock private VentilationState ventilationState;
  @Mock private Gathering gathering1;
  @Mock private Gathering gathering2;
  @Mock private Intervention intervention1;

  private GatheringID gatheringID1;
  private InterventionID interventionID1;

  @BeforeEach
  void setUp() {
    gatherings = new ArrayList<>();
    interventions = new ArrayList<>();
    zoneState = new ZoneState(FireState.NONE, false, ventilationState, gatherings, interventions);

    gatheringID1 = new GatheringID("GATHERING-001");
    interventionID1 = new InterventionID("INTERVENTION-001");
  }

  @Test
  void givenZoneStateWithNoInterventions_whenGetNumberOfAgentsToSend_thenReturnFour() {
    int result = zoneState.getNumberOfAgentsToSend(InterventionReason.FIREALARM);
    assertEquals(4, result);
  }

  @Test
  void
      givenZoneStateWithInterventionMatchingReason_whenGetNumberOfAgentsToSend_thenReturnRemainingAgents() {
    when(intervention1.getReason()).thenReturn(InterventionReason.FIREALARM);
    when(intervention1.getAgentCount()).thenReturn(2);
    interventions.add(intervention1);

    int result = zoneState.getNumberOfAgentsToSend(InterventionReason.FIREALARM);
    assertEquals(2, result);
  }

  @Test
  void
      givenZoneStateWithInterventionNonMatchingReason_whenGetNumberOfAgentsToSend_thenReturnCorrectAmountOfAgents() {
    when(intervention1.getReason()).thenReturn(InterventionReason.PREALARM);
    when(intervention1.getAgentCount()).thenReturn(1);
    interventions.add(intervention1);

    int result = zoneState.getNumberOfAgentsToSend(InterventionReason.PREALARM);
    assertEquals(3, result);
  }

  @Test
  void givenZoneStateWithInterventions_whenGetNumberOfAgents_thenReturnTotalAgentCount() {
    when(intervention1.getAgentCount()).thenReturn(2);
    interventions.add(intervention1);

    Intervention intervention2 = mock(Intervention.class);
    when(intervention2.getAgentCount()).thenReturn(3);
    interventions.add(intervention2);

    int result = zoneState.getNumberOfAgents();
    assertEquals(5, result);
  }

  @Test
  void givenZoneStateWithMatchingGathering_whenHasGathering_thenReturnTrue() {
    when(gathering1.getType()).thenReturn(GatheringType.ACADEMIC);
    gatherings.add(gathering1);

    boolean result = zoneState.hasGathering(GatheringType.ACADEMIC);
    assertTrue(result);
  }

  @Test
  void givenZoneStateWithoutMatchingGathering_whenHasGathering_thenReturnFalse() {
    when(gathering1.getType()).thenReturn(GatheringType.ACADEMIC);
    gatherings.add(gathering1);

    boolean result = zoneState.hasGathering(GatheringType.OTHER);
    assertFalse(result);
  }

  @Test
  void givenZoneState_whenAddGathering_thenGatheringIsAdded() {
    zoneState.addGathering(gathering1);

    assertTrue(zoneState.getGatherings().contains(gathering1));
  }

  @Test
  void givenZoneStateWithExistingGathering_whenAddGathering_thenThrowException() {
    when(gathering1.getId()).thenReturn(gatheringID1);
    gatherings.add(gathering1);

    assertThrows(GatheringAlreadyExistsException.class, () -> zoneState.addGathering(gathering1));
  }

  @Test
  void givenExistingGatheringId_whenRemoveGathering_thenGatheringIsRemoved() {
    when(gathering1.getId()).thenReturn(gatheringID1);
    gatherings.add(gathering1);

    zoneState.removeGathering(gatheringID1);

    assertFalse(zoneState.getGatherings().contains(gathering1));
  }

  @Test
  void givenNonExistingGatheringId_whenRemoveGathering_thenThrowException() {
    assertThrows(GatheringNotFoundException.class, () -> zoneState.removeGathering(gatheringID1));
  }

  @Test
  void givenZoneStateWithLargeGathering_whenHasLargeGroup_thenReturnTrue() {
    when(gathering1.getExpectedPeopleCount()).thenReturn(51);
    gatherings.add(gathering1);

    boolean result = zoneState.hasLargeGroup();
    assertTrue(result);
  }

  @Test
  void givenZoneStateWithMultipleGatherings_whenGetTotalExpectedPeopleCount_thenReturnSum() {
    when(gathering1.getExpectedPeopleCount()).thenReturn(30);
    when(gathering2.getExpectedPeopleCount()).thenReturn(25);
    gatherings.add(gathering1);
    gatherings.add(gathering2);

    int result = zoneState.getTotalExpectedPeopleCount();
    assertEquals(55, result);
  }

  @Test
  void givenNewIntervention_whenAddIntervention_thenInterventionIsAdded() {
    zoneState.addIntervention(intervention1);

    assertTrue(zoneState.getInterventions().contains(intervention1));
  }

  @Test
  void givenExistingIntervention_whenAddIntervention_thenThrowException() {
    when(intervention1.getId()).thenReturn(interventionID1);
    interventions.add(intervention1);

    assertThrows(
        InterventionAlreadyExistsException.class, () -> zoneState.addIntervention(intervention1));
  }

  @Test
  void givenExistingIntervention_whenRemoveIntervention_thenInterventionIsRemoved() {
    when(intervention1.getId()).thenReturn(interventionID1);
    interventions.add(intervention1);

    zoneState.removeIntervention(interventionID1);
    assertFalse(zoneState.getInterventions().contains(intervention1));
  }

  @Test
  void givenNonExistingIntervention_whenRemoveIntervention_thenThrowException() {
    assertThrows(
        InterventionNotFoundException.class, () -> zoneState.removeIntervention(interventionID1));
  }

  @Test
  void givenExistingIntervention_whenGetInterventionReason_thenReturnReason() {
    when(intervention1.getId()).thenReturn(interventionID1);
    when(intervention1.getReason()).thenReturn(InterventionReason.FIREALARM);
    interventions.add(intervention1);

    InterventionReason result = zoneState.getInterventionReason(interventionID1);
    assertEquals(InterventionReason.FIREALARM, result);
  }

  @Test
  void givenNonExistingIntervention_whenGetInterventionReason_thenThrowException() {
    assertThrows(
        InterventionNotFoundException.class,
        () -> zoneState.getInterventionReason(interventionID1));
  }
}
