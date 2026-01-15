package ca.ulaval.glo4002.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NoCardEntryTest {

  private NoCardEntry noCardEntry;

  @Mock private Building building;
  @Mock private DateTime validTime;
  @Mock private ZoneID zoneId;
  @Mock private RoomID roomId;
  @Mock private Action returnedAction;
  @Mock private EventContext eventContext;

  @BeforeEach
  void setUp() {
    noCardEntry = new NoCardEntry(validTime, zoneId, roomId);
  }

  @Test
  void givenBuilding_whenHandleEvent_thenReturnNoCardEntryActions() {
    List<Action> actionsReturned = List.of(returnedAction);
    when(building.handleNoCardEntry(eventContext, roomId)).thenReturn(actionsReturned);

    List<Action> actions = noCardEntry.handleEvent(building, eventContext);

    assertEquals(actionsReturned, actions);
  }
}
