package ca.ulaval.glo4002.application.infrastructure.persistence.mappers;

import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.RoomStateEntity;

public class RoomStateMapper {
  public RoomStateEntity toStateEntity(RoomID id, RoomState room) {
    return new RoomStateEntity(
        id,
        room.isElectricityOpen(),
        room.getCurrentOccupancy(),
        room.getIdentifiedOccupants(),
        room.getConsecutiveAccesses());
  }

  public RoomState toState(RoomStateEntity room) {
    return new RoomState(
        room.is_electricity_open(),
        room.currentOccupancy(),
        room.identifiedOccupants(),
        room.consecutiveAccesses());
  }
}
