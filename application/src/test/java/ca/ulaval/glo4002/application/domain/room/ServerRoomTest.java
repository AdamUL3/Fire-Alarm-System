package ca.ulaval.glo4002.application.domain.room;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerRoomTest {

  private static final String ROOM_ID = "348798";
  private static final String ZONE_ID = "32244";
  private static final String BUILDING_ID = "92";

  @Mock private RoomActionFactory roomActionFactory;
  @Mock private RoomState roomState;
  @Mock private RoomState initialRoomState;
  @Mock private ActionContext actionContext;

  private ServerRoom serverRoom;
  private RoomID roomId;
  private ZoneID zoneId;
  private BuildingID buildingId;

  @BeforeEach
  void setUp() {
    roomId = new RoomID(ROOM_ID);
    zoneId = new ZoneID(ZONE_ID);
    buildingId = new BuildingID(BUILDING_ID);
    serverRoom = new ServerRoom(roomId, roomState, roomActionFactory);
  }

  @Test
  void givenServerRoomAndElectricityIsOpen_whenHandleFireAlarm_thenClosePower() {
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(roomState.isElectricityOpen()).thenReturn(false);

    serverRoom.handleFireAlarm(actionContext);

    verify(roomActionFactory)
        .createOpenClosePower(buildingId, zoneId, roomId, OpenClosedState.CLOSE);
  }

  @Test
  void givenServerRoomStateChange_whenHandleFireAlarmCompleted_thenRevertToInitialState() {
    when(actionContext.getBuildingID()).thenReturn(buildingId);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(roomState.isElectricityOpen()).thenReturn(false);
    when(initialRoomState.isElectricityOpen()).thenReturn(true);

    serverRoom.handleFireAlarmCompleted(actionContext, initialRoomState);

    verify(roomActionFactory)
        .createOpenClosePower(buildingId, zoneId, roomId, OpenClosedState.OPEN);
  }
}
