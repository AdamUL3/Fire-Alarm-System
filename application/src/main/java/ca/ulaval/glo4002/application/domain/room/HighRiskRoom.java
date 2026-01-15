package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.zone.AgentDispatcher;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;

public class HighRiskRoom extends Room {
  private final AgentDispatcher agentDispatcher;
  private final AlarmActionFactory alarmActionFactory;

  public HighRiskRoom(
      RoomID id,
      RoomState state,
      RoomActionFactory roomActionFactory,
      AgentDispatcher agentDispatcher,
      AlarmActionFactory alarmActionFactory) {
    super(id, state, roomActionFactory);
    this.agentDispatcher = agentDispatcher;
    this.alarmActionFactory = alarmActionFactory;
  }

  public Intervention handleNoCardEntry(ActionContext actionContext) {
    actionContext.addAction(
        alarmActionFactory.createActivateFireAlarm(
            actionContext.getBuildingID(), actionContext.getZoneID(), AlarmState.RING));
    state.addOccupant(null);
    return agentDispatcher.sendPatrolAgents(
        1, AgentPriority.P1, InterventionReason.INTRUSION, actionContext);
  }
}
