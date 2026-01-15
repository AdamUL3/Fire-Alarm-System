package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;

public class ServerRoom extends Room {
  public ServerRoom(RoomID id, RoomState state, RoomActionFactory roomActionFactory) {
    super(id, state, roomActionFactory);
  }

  @Override
  public void handleFireAlarm(ActionContext actionContext) {
    if (!state.isElectricityOpen()) {
      actionContext.addAction(
          roomActionFactory.createOpenClosePower(
              actionContext.getBuildingID(), actionContext.getZoneID(), id, OpenClosedState.CLOSE));
    }
  }

  @Override
  public void handleFireAlarmCompleted(ActionContext actionContext, RoomState initialRoomState) {
    if (state.isElectricityOpen() != initialRoomState.isElectricityOpen()) {
      actionContext.addAction(
          roomActionFactory.createOpenClosePower(
              actionContext.getBuildingID(),
              actionContext.getZoneID(),
              id,
              OpenClosedState.getState(initialRoomState.isElectricityOpen())));
    }
  }
}
