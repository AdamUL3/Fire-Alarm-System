package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.exception.DomainException;

public class RoomNotFoundException extends DomainException {
  public RoomNotFoundException(RoomID roomId) {
    super(String.format("Room with ID %s not found", roomId.toString()), "ROOM_NOT_FOUND");
  }
}
