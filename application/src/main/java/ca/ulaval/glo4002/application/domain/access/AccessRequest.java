package ca.ulaval.glo4002.application.domain.access;

import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.event.DateTime;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class AccessRequest {
  private final CardID cardId;
  private final ZoneID zoneId;
  private final RoomID roomId;
  private final DoorID doorId;
  private final DateTime date;

  public AccessRequest(CardID cardId, ZoneID zoneId, RoomID roomId, DoorID doorId, DateTime date) {
    this.cardId = cardId;
    this.zoneId = zoneId;
    this.roomId = roomId;
    this.doorId = doorId;
    this.date = date;
  }

  public CardID getCardId() {
    return cardId;
  }

  public ZoneID getZoneId() {
    return zoneId;
  }

  public RoomID getRoomId() {
    return roomId;
  }

  public DoorID getDoorId() {
    return doorId;
  }

  public void handleAccess(Building building, PersonAccessInformation accessCardInfo) {
    building.handleAccess(zoneId, roomId, doorId, accessCardInfo);
  }
}
