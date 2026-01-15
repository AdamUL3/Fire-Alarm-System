package ca.ulaval.glo4002.application.domain.door;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FireDoorTest {

  private FireDoor fireDoor;
  private final DoorID doorId = new DoorID("FIRE1");

  @Mock private DoorState doorState;
  @Mock private DoorState initialDoorState;
  @Mock private DoorActionFactory doorActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private Action returnedAction;
  @Mock private BuildingID buildingId;
  @Mock private ZoneID zoneId;

  private static final boolean IS_NOT_ON_EVACUATION_PATH = false;

  @BeforeEach
  void setUp() {
    fireDoor = new FireDoor(doorId, doorState, IS_NOT_ON_EVACUATION_PATH, doorActionFactory);
  }

  @Test
  void givenDoorIsClosed_whenHandleFireAlarm_thenSetDoorStateToOpen() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(doorActionFactory.createOpenCloseDoor(buildingId, zoneId, doorId, OpenClosedState.OPEN))
        .thenReturn(returnedAction);

    fireDoor.handleFireAlarm(actionContext);

    verify(doorState).setOpenClosedState(OpenClosedState.OPEN);
    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenDoorIsOpen_whenHandleFireAlarm_thenDoesNotSetOpenStateAgain() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.OPEN);

    fireDoor.handleFireAlarm(actionContext);

    verify(doorState, never()).setOpenClosedState(OpenClosedState.OPEN);
  }

  @Test
  void
      givenDoorStateChanged_whenHandleFireAlarmCompleted_thenRevertOpenClosedStateToInitialState() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.OPEN);
    when(initialDoorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(doorActionFactory.createOpenCloseDoor(buildingId, zoneId, doorId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    fireDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorState).setOpenClosedState(OpenClosedState.CLOSE);
    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenDoorStateNotChanged_whenHandleFireAlarmCompleted_thenDoesNotAddAction() {
    when(doorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);
    when(initialDoorState.getOpenClosedState()).thenReturn(OpenClosedState.CLOSE);

    fireDoor.handleFireAlarmCompleted(actionContext, initialDoorState);

    verify(doorActionFactory, never()).createOpenCloseDoor(any(), any(), any(), any());
    verify(actionContext, never()).addAction(any());
  }

  @Test
  void whenHandleProtest_thenDoesNothing() {
    fireDoor.handleProtest(actionContext);

    verifyNoInteractions(actionContext, doorState, doorActionFactory);
  }
}
