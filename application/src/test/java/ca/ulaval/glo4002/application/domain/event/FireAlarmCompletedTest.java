package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class FireAlarmCompletedTest {
  FireAlarmCompleted fireAlarmCompleted;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private EventContext eventContext;

  @BeforeEach
  public void setUp() {
    fireAlarmCompleted = new FireAlarmCompleted(validTime, zoneId);
  }

  @Test
  void givenPreviousActions_whenHandleEvent_thenReturnFireAlarmCompletedActions() {
    List<Action> actions = fireAlarmCompleted.handleEvent(building, eventContext);

    assertNotNull(actions);
  }
}
