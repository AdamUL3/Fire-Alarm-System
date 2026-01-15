package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.access.AccessRequest;
import ca.ulaval.glo4002.application.domain.access.CardID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.event.DateTime;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.interfaces.rest.dto.AccessRequestDTO;

public class AccessRequestMapper {

  public AccessRequest toAccessRequest(AccessRequestDTO accessRequestDTO) {
    return new AccessRequest(
        new CardID(accessRequestDTO.getCardId()),
        new ZoneID(accessRequestDTO.getZoneId()),
        new RoomID(accessRequestDTO.getRoomId()),
        new DoorID(accessRequestDTO.getDoorId()),
        new DateTime(accessRequestDTO.getDateTime()));
  }
}
