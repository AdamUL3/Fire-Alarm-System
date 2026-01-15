package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.*;
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
class ExpiredPreAlarmTest {
  private ExpiredPreAlarm expiredPrealarm;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private Action returnedAction;
  @Mock private EventContext eventContext;

  @BeforeEach
  void setUp() {
    expiredPrealarm = new ExpiredPreAlarm(validTime, zoneId);
  }

  @Test
  void givenBuilding_whenHandleEvent_thenReturnActionsFromConfirmedPreAlarm() {
    List<Action> expectedAction = List.of(returnedAction);
    when(building.handleFireAlarm(eventContext)).thenReturn(expectedAction);

    List<Action> actions = expiredPrealarm.handleEvent(building, eventContext);

    assertEquals(actions, expectedAction);
  }
}
