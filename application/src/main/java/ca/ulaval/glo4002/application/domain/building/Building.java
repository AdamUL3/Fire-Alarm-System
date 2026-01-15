package ca.ulaval.glo4002.application.domain.building;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.event.EventContext;
import ca.ulaval.glo4002.application.domain.event.SmokeThreshold;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.zone.FireState;
import ca.ulaval.glo4002.application.domain.zone.Zone;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.exception.NoEmergencyInZoneException;
import ca.ulaval.glo4002.application.domain.zone.exception.ZoneNotFoundException;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import java.util.ArrayList;
import java.util.List;

public class Building {
  private final BuildingID id;
  private final BuildingAddress address;
  private final List<Zone> zones;
  private final Ventilation ventilation;
  private final FirefighterActionFactory firefighterActionFactory;
  private final ActionContextFactory actionContextFactory;

  public Building(
      BuildingID id,
      BuildingAddress address,
      List<Zone> zones,
      Ventilation ventilation,
      FirefighterActionFactory actionFactory,
      ActionContextFactory actionContextFactory) {
    this.id = id;
    this.address = address;
    this.ventilation = ventilation;
    this.zones = zones;
    this.firefighterActionFactory = actionFactory;
    this.actionContextFactory = actionContextFactory;
  }

  public BuildingID getId() {
    return id;
  }

  public List<Zone> getZones() {
    return zones;
  }

  private Zone getZoneById(ZoneID zoneId) {
    for (Zone zone : zones) {
      if (zone.getId().equals(zoneId)) {
        return zone;
      }
    }
    throw new ZoneNotFoundException(zoneId);
  }

  public List<Action> handlePreAlarm(EventContext eventContext) {
    if (containsZoneWithLargeGroup()) {
      return handleFireAlarm(eventContext);
    }
    List<Action> actions = new ArrayList<>();
    zones.forEach(
        zone -> {
          if (zone.getId().equals(eventContext.activeZoneId())) {
            zone.setFireState(FireState.PROBABLE);
          }
          ActionContext actionContext = actionContextFactory.createActionContext(id, zone.getId());
          zone.handlePreAlarm(ventilation, actionContext);
          actions.addAll(actionContext.getActions());
        });
    return actions;
  }

  public List<Action> handleFireAlarm(EventContext eventContext) {
    List<Action> actions = new ArrayList<>();
    zones.forEach(
        zone -> {
          if (zone.getId().equals(eventContext.activeZoneId())) {
            zone.setFireState(FireState.CONFIRMED);
          }
          ActionContext actionContext = actionContextFactory.createActionContext(id, zone.getId());
          zone.handleFireAlarm(ventilation, actionContext);
          actions.addAll(actionContext.getActions());
        });
    actions.add(firefighterActionFactory.createCallFirefighters(address, CallReasonType.FIRE));
    return actions;
  }

  public List<Action> handleFireAlarmCompleted(EventContext eventContext) {
    List<Action> actions = new ArrayList<>();
    zones.forEach(
        zone -> {
          if (zone.getId().equals(eventContext.activeZoneId())) {
            zone.setFireState(FireState.NONE);
          }
          ActionContext actionContext = actionContextFactory.createActionContext(id, zone.getId());
          zone.handleFireAlarmCompleted(
              ventilation,
              actionContext,
              eventContext
                  .initialBuildingState()
                  .orElseThrow(() -> new NoEmergencyInZoneException(zone.getId()))
                  .zoneStates()
                  .get(zone.getId()));
          actions.addAll(actionContext.getActions());
        });
    return actions;
  }

  public List<Action> handleCancelledPreAlarm(EventContext eventContext) {
    List<Action> actions = new ArrayList<>();
    zones.forEach(
        zone -> {
          if (zone.getId().equals(eventContext.activeZoneId())) {
            zone.setFireState(FireState.NONE);
          }
          ActionContext actionContext = actionContextFactory.createActionContext(id, zone.getId());
          zone.handleCancelledPreAlarm(
              ventilation,
              actionContext,
              eventContext
                  .initialBuildingState()
                  .orElseThrow(() -> new NoEmergencyInZoneException(zone.getId()))
                  .zoneStates()
                  .get(zone.getId()));
          actions.addAll(actionContext.getActions());
        });
    return actions;
  }

  public List<Action> handleSmokePresence(int smokeConcentration, EventContext eventContext) {
    getZoneById(eventContext.activeZoneId()).setSmokePresence(true);
    if (smokeConcentration <= SmokeThreshold.THRESHOLD_1.getThreshold()) {
      return handlePreAlarm(eventContext);
    }
    return handleFireAlarm(eventContext);
  }

  public List<Action> handleNoCardEntry(EventContext eventContext, RoomID roomId) {
    ActionContext actionContext =
        actionContextFactory.createActionContext(id, eventContext.activeZoneId());
    this.getZoneById(eventContext.activeZoneId()).handleNoCardEntry(roomId, actionContext);
    return actionContext.getActions();
  }

  public List<Action> handleExit(EventContext eventContext, RoomID roomId) {
    this.getZoneById(eventContext.activeZoneId()).handleExitRoom(roomId);
    return List.of();
  }

  public List<Action> handleGatheringStart(
      EventContext eventContext,
      GatheringID gatheringId,
      int expectedPeopleCount,
      GatheringType gatheringType) {
    ActionContext actionContext =
        actionContextFactory.createActionContext(id, eventContext.activeZoneId());
    this.getZoneById(eventContext.activeZoneId())
        .handleGatheringStart(actionContext, gatheringId, expectedPeopleCount, gatheringType);
    return actionContext.getActions();
  }

  public List<Action> handleGatheringEnd(EventContext eventContext, GatheringID gatheringId) {
    ActionContext actionContext =
        actionContextFactory.createActionContext(id, eventContext.activeZoneId());
    this.getZoneById(eventContext.activeZoneId()).handleGatheringEnd(gatheringId);
    return actionContext.getActions();
  }

  public List<Action> handleAgentArrived(EventContext eventContext, InterventionID interventionId) {
    ActionContext actionContext =
        actionContextFactory.createActionContext(id, eventContext.activeZoneId());
    this.getZoneById(eventContext.activeZoneId()).handleAgentArrived(interventionId, actionContext);
    return actionContext.getActions();
  }

  public List<Action> handleAgentLeft(EventContext eventContext, InterventionID interventionId) {
    this.getZoneById(eventContext.activeZoneId()).handleAgentLeft(interventionId);
    return List.of();
  }

  public List<Action> handleAgentSent(EventContext eventContext, InterventionID interventionId) {
    this.getZoneById(eventContext.activeZoneId()).handleAgentSent(interventionId);
    return List.of();
  }

  private boolean containsZoneWithLargeGroup() {
    for (Zone zone : zones) {
      if (zone.hasLargeGroup()) {
        return true;
      }
    }
    return false;
  }

  public void handleAccess(
      ZoneID zoneId, RoomID roomId, DoorID doorId, PersonAccessInformation personAccessInfo) {
    if (personAccessInfo.getCardId() == null) {
      throw new RefusedAccessException();
    }
    if (isLabResponsible(personAccessInfo.getPersonId())) {
      personAccessInfo.setLabResponsibleInBuilding(true);
    }
    getZoneById(zoneId).handleAccess(roomId, doorId, personAccessInfo);
  }

  private boolean isLabResponsible(PersonID personID) {
    for (Zone zone : zones) {
      if (zone.isLabResponsibleInZone(personID)) {
        return true;
      }
    }
    return false;
  }
}
