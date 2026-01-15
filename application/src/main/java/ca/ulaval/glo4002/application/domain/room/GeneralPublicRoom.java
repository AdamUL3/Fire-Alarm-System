package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;

public class GeneralPublicRoom extends Room {
  public GeneralPublicRoom(RoomID id, RoomState state, RoomActionFactory roomActionFactory) {
    super(id, state, roomActionFactory);
  }
}
