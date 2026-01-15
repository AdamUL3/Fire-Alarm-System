package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.RoomActionFactory;
import ca.ulaval.glo4002.application.domain.zone.AgentDispatcher;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;

public class RiskLimitedRoom extends Room {
  private final AgentDispatcher agentDispatcher;

  public RiskLimitedRoom(
      RoomID id,
      RoomState state,
      RoomActionFactory roomActionFactory,
      AgentDispatcher agentDispatcher) {
    super(id, state, roomActionFactory);
    this.agentDispatcher = agentDispatcher;
  }

  public Intervention handleNoCardEntry(ActionContext actionContext) {
    state.addOccupant(null);
    return agentDispatcher.sendPatrolAgents(
        1, AgentPriority.P1, InterventionReason.INTRUSION, actionContext);
  }
}
