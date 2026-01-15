package ca.ulaval.glo4002.application.domain.zone;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.door.Door;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.door.DoorNotFoundException;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomNotFoundException;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContext;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationState;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import java.util.List;

public class Zone {
  protected final ZoneID id;
  protected final List<Door> doors;
  protected final List<Room> rooms;
  protected final ZoneState state;
  protected final AgentDispatcher agentDispatcher;
  protected final AlarmActionFactory alarmActionFactory;
  protected final GatheringFactory gatheringFactory;
  protected final VentilationContextFactory ventilationContextFactory;

  public Zone(
      ZoneID id,
      List<Door> doors,
      List<Room> rooms,
      ZoneState state,
      AgentDispatcher agentDispatcher,
      AlarmActionFactory alarmActionFactory,
      GatheringFactory gatheringFactory,
      VentilationContextFactory ventilationContextFactory) {
    this.id = id;
    this.doors = doors;
    this.rooms = rooms;
    this.state = state;
    this.agentDispatcher = agentDispatcher;
    this.alarmActionFactory = alarmActionFactory;
    this.gatheringFactory = gatheringFactory;
    this.ventilationContextFactory = ventilationContextFactory;
  }

  public ZoneID getId() {
    return id;
  }

  public List<Door> getDoors() {
    return doors;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public ZoneState getState() {
    return state;
  }

  private Room getRoomById(RoomID roomId) {
    for (Room room : rooms) {
      if (room.getId().equals(roomId)) {
        return room;
      }
    }
    throw new RoomNotFoundException(roomId);
  }

  private Door getDoorById(DoorID doorId) {
    for (Door door : doors) {
      if (door.getId().equals(doorId)) {
        return door;
      }
    }
    throw new DoorNotFoundException(doorId);
  }

  public void setFireState(FireState fireState) {
    state.setFireState(fireState);
  }

  public void setSmokePresence(boolean smokePresence) {
    state.setSmokePresence(smokePresence);
  }

  public void handleFireAlarmCompleted(
      Ventilation ventilation, ActionContext actionContext, AggregateZoneState initialZoneState) {
    if (state.getFireState().hasEmergency()) {
      return;
    }
    ventilation.revert(
        state.getVentilationState(),
        initialZoneState.zoneState().getVentilationState(),
        actionContext);

    doors.forEach(
        door ->
            door.handleFireAlarmCompleted(
                actionContext, initialZoneState.doorStates().get(door.getId())));
    rooms.forEach(
        room ->
            room.handleFireAlarmCompleted(
                actionContext, initialZoneState.roomStates().get(room.getId())));
  }

  public void handleFireAlarm(Ventilation ventilation, ActionContext actionContext) {
    sendAgentsForFireAlarm(actionContext);
    adjustVentilation(
        ventilation,
        ventilationContextFactory.createProbableEmergencyInRegularZone(
            state.getFireState().hasEmergency()),
        actionContext);

    actionContext.addAction(
        alarmActionFactory.createActivateFireAlarm(
            actionContext.getBuildingID(), id, AlarmState.RING));

    doors.forEach(door -> door.handleFireAlarm(actionContext));
    rooms.forEach(room -> room.handleFireAlarm(actionContext));
  }

  public void handlePreAlarm(Ventilation ventilation, ActionContext actionContext) {
    adjustVentilation(
        ventilation,
        ventilationContextFactory.createProbableEmergencyInRegularZone(
            state.getFireState().hasEmergency()),
        actionContext);
    if (state.getFireState().hasEmergency()) {
      state.addIntervention(
          agentDispatcher.sendPatrolAgents(
              1, AgentPriority.P1, InterventionReason.PREALARM, actionContext));
    }
  }

  public void handleCancelledPreAlarm(
      Ventilation ventilation, ActionContext actionContext, AggregateZoneState initialZoneState) {
    if (state.getFireState().hasEmergency()) {
      return;
    }
    revertVentilation(
        ventilation, initialZoneState.zoneState().getVentilationState(), actionContext);
  }

  protected void adjustVentilation(
      Ventilation ventilation, VentilationContext ventilationContext, ActionContext actionContext) {

    ventilation.adjust(state.getVentilationState(), ventilationContext, actionContext);
  }

  private void revertVentilation(
      Ventilation ventilation,
      VentilationState initialVentilationState,
      ActionContext actionContext) {
    ventilation.revert(state.getVentilationState(), initialVentilationState, actionContext);
  }

  public void handleNoCardEntry(RoomID roomId, ActionContext actionContext) {
    Intervention intervention = this.getRoomById(roomId).handleNoCardEntry(actionContext);
    if (intervention != null) {
      state.addIntervention(intervention);
    }
  }

  public void handleGatheringStart(
      ActionContext actionContext,
      GatheringID gatheringId,
      int expectedPeopleCount,
      GatheringType gatheringType) {
    Gathering gathering =
        gatheringFactory.createGathering(gatheringId, expectedPeopleCount, gatheringType);
    state.addGathering(gathering);
    if (gatheringType == GatheringType.PROTEST) {
      state.addIntervention(
          agentDispatcher.sendPatrolAgents(
              2, AgentPriority.P2, InterventionReason.GATHERING, actionContext));
      for (Door door : doors) {
        door.handleProtest(actionContext);
      }
    } else if (gatheringType == GatheringType.OTHER_RISK) {
      state.addIntervention(
          agentDispatcher.sendPatrolAgents(
              1, AgentPriority.P2, InterventionReason.GATHERING, actionContext));
    } else if (hasLargeGroup()) {
      state.addIntervention(
          agentDispatcher.sendPatrolAgents(
              1, AgentPriority.P3, InterventionReason.GATHERING, actionContext));
    }
  }

  public void handleGatheringEnd(GatheringID gatheringID) {
    state.removeGathering(gatheringID);
  }

  public boolean hasLargeGroup() {
    return state.hasLargeGroup();
  }

  public void handleAgentArrived(InterventionID interventionId, ActionContext actionContext) {
    if (state.getInterventionReason(interventionId).equals(InterventionReason.INTRUSION)) {
      actionContext.addAction(
          alarmActionFactory.createActivateFireAlarm(
              actionContext.getBuildingID(), id, AlarmState.STOP));
    }
  }

  public void handleExitRoom(RoomID roomId) {
    getRoomById(roomId).handleExitRoom();
  }

  public void handleAgentLeft(InterventionID interventionId) {
    state.removeIntervention(interventionId);
  }

  public void handleAgentSent(InterventionID interventionId) {}

  public boolean isLabResponsibleInZone(PersonID personID) {
    for (Room room : rooms) {
      if (room.isLabResponsible(personID)) {
        return true;
      }
    }
    return false;
  }

  public void handleAccess(RoomID roomId, DoorID doorId, PersonAccessInformation personAccessInfo) {
    Room room = null;
    Door door = null;
    if (doorId != null) {
      door = getDoorById(doorId);
    }
    if (roomId != null) {
      room = getRoomById(roomId);
    }
    if (state.getVentilationState().hasExtremePressure()) {
      if (room == null) {
        throw new RefusedAccessException();
      }
      if (!room.consecutiveAttemptSuccess(personAccessInfo)) {
        throw new RefusedAccessException();
      }
    }
    if (doorId != null) {
      if (state.getFireState() == FireState.CONFIRMED) {
        door.handleAccessDuringEmergency(personAccessInfo);
      }
    }
    if (state.getFireState().hasEmergency()) {
      if (personAccessInfo.getRole() == AccessRole.SECURITY_AGENT) {
      } else if (room != null) {
        room.handleAccessDuringEmergency(personAccessInfo);
      } else if (isLabResponsibleInZone(personAccessInfo.getPersonId())) {
      } else {
        throw new RefusedAccessException();
      }
    }
    if (roomId != null) {
      room.addOccupant(personAccessInfo.getPersonId());
    }
  }

  protected void sendAgentsForFireAlarm(ActionContext actionContext) {
    if (state.getFireState().hasEmergency()) {
      if (hasLargeGroup()) {
        state.addIntervention(
            agentDispatcher.sendPatrolAgentsLargeGroupGathering(
                state.getTotalExpectedPeopleCount(),
                AgentPriority.P1,
                InterventionReason.FIREALARM,
                actionContext));
      } else if (state.hasGathering(GatheringType.OTHER_RISK)
          || state.hasGathering(GatheringType.SOCIAL_ACTIVITY)) {
        state.addIntervention(
            agentDispatcher.sendPatrolAgentsNormalGathering(
                state.getTotalExpectedPeopleCount(),
                AgentPriority.P1,
                InterventionReason.FIREALARM,
                actionContext));
      } else {
        state.addIntervention(
            agentDispatcher.sendPatrolAgents(
                state.getNumberOfAgentsToSend(InterventionReason.PREALARM),
                AgentPriority.P1,
                InterventionReason.FIREALARM,
                actionContext));
      }
    }
  }
}
