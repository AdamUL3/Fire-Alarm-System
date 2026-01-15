package ca.ulaval.glo4002.application.domain.zone;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.AlarmActionFactory;
import ca.ulaval.glo4002.application.domain.action.AlarmState;
import ca.ulaval.glo4002.application.domain.door.Door;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContext;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import java.util.List;

public class HazardousZone extends Zone {

  public HazardousZone(
      ZoneID id,
      List<Door> doors,
      List<Room> rooms,
      ZoneState state,
      AgentDispatcher agentDispatcher,
      AlarmActionFactory alarmActionFactory,
      GatheringFactory gatheringFactory,
      VentilationContextFactory ventilationContextFactory) {
    super(
        id,
        doors,
        rooms,
        state,
        agentDispatcher,
        alarmActionFactory,
        gatheringFactory,
        ventilationContextFactory);
  }

  @Override
  public void handlePreAlarm(Ventilation ventilation, ActionContext actionContext) {
    VentilationContext ventilationContext;
    if (!isOccupied()) {
      ventilationContext =
          ventilationContextFactory.createProbableEmergencyInUnoccupiedHazardousZone(
              state.getFireState().hasEmergency(), doors.size());
    } else {
      ventilationContext =
          ventilationContextFactory.createProbableEmergencyInHazardousZone(
              state.getFireState().hasEmergency());
    }
    adjustVentilation(ventilation, ventilationContext, actionContext);
    if (state.getFireState().hasEmergency()) {
      state.addIntervention(
          agentDispatcher.sendPatrolAgents(
              1, AgentPriority.P1, InterventionReason.PREALARM, actionContext));
    }
  }

  @Override
  public void handleFireAlarm(Ventilation ventilation, ActionContext actionContext) {

    sendAgentsForFireAlarm(actionContext);

    adjustVentilation(
        ventilation,
        ventilationContextFactory.createProbableEmergencyInHazardousZone(
            state.getFireState().hasEmergency()),
        actionContext);

    actionContext.addAction(
        alarmActionFactory.createActivateFireAlarm(
            actionContext.getBuildingID(), id, AlarmState.RING));

    doors.forEach(door -> door.handleFireAlarm(actionContext));
    rooms.forEach(room -> room.handleFireAlarm(actionContext));
  }

  private boolean isOccupied() {
    for (Room room : rooms) {
      if (room.getState().getCurrentOccupancy() > 0) {
        return true;
      }
    }
    return false;
  }
}
