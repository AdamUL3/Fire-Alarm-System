package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GatheringStartTest {

  private GatheringStart gatheringStart;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private GatheringID gatheringId;
  @Mock private Action returnedAction;
  @Mock private EventContext eventContext;

  private static final int EXPECTED_NUMBER_OF_PEOPLE = 25;
  private static final GatheringType GATHERING_TYPE = GatheringType.ACADEMIC;

  @BeforeEach
  void setUp() {
    gatheringStart =
        new GatheringStart(
            validTime, zoneId, gatheringId, EXPECTED_NUMBER_OF_PEOPLE, GATHERING_TYPE);
  }

  @Test
  void givenBuildingAndGatheringDetails_whenHandleEvent_thenReturnGatheringStartActions() {
    List<Action> expectedActions = List.of(returnedAction);
    when(building.handleGatheringStart(
            eventContext, gatheringId, EXPECTED_NUMBER_OF_PEOPLE, GATHERING_TYPE))
        .thenReturn(expectedActions);

    List<Action> result = gatheringStart.handleEvent(building, eventContext);

    assertEquals(expectedActions, result);
  }
}
