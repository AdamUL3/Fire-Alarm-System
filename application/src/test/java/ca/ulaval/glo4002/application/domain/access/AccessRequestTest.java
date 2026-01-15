package ca.ulaval.glo4002.application.domain.access;

import static org.mockito.Mockito.verify;

import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.event.DateTime;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessRequestTest {

  private ZoneID zoneId;
  private RoomID roomId;
  private DoorID doorId;
  private AccessRequest accessRequest;

  @Mock private Building building;
  @Mock private PersonAccessInformation accessCardInfo;

  @BeforeEach
  void setUp() {
    CardID cardId = new CardID("CARD-123");
    zoneId = new ZoneID("ZONE-1");
    roomId = new RoomID("ROOM-42");
    doorId = new DoorID("DOOR-7");
    DateTime date = new DateTime("2025-12-05 00:00:00");
    accessRequest = new AccessRequest(cardId, zoneId, roomId, doorId, date);
  }

  @Test
  void
      givenValidAccessRequest_whenHandleAccess_thenBuildingHandleAccessCalledWithCorrectParameters() {
    accessRequest.handleAccess(building, accessCardInfo);

    verify(building).handleAccess(zoneId, roomId, doorId, accessCardInfo);
  }
}
