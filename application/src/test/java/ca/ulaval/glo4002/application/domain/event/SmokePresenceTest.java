package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SmokePresenceTest {
  private SmokePresence smokePresence;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private Action returnedAction;
  @Mock private EventContext eventContext;

  private static final int SMOKE_CONCENTRATION = 50;

  @BeforeEach
  void setUp() {
    smokePresence = new SmokePresence(validTime, zoneId, SMOKE_CONCENTRATION);
  }

  @Test
  void givenBuilding_whenHandleEvent_thenReturnSmokePresenceActions() {
    List<Action> actionsReturned = List.of(returnedAction);
    when(building.handleSmokePresence(SMOKE_CONCENTRATION, eventContext))
        .thenReturn(actionsReturned);

    List<Action> actions = smokePresence.handleEvent(building, eventContext);

    assertEquals(actions, actionsReturned);
  }
}
