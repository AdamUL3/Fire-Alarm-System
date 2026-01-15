package ca.ulaval.glo4002.application.infrastructure.persistence.mappers;

import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.door.DoorState;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContext;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationSpeedState;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationState;
import ca.ulaval.glo4002.application.domain.zone.AggregateZoneState;
import ca.ulaval.glo4002.application.domain.zone.FireState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.ZoneState;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.ZoneDTO;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.DoorStateEntity;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.RoomStateEntity;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.ZoneStateEntity;
import ca.ulaval.glo4002.application.interfaces.rest.dto.ZoneStateDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZoneStateMapper {
  private final DoorStateMapper doorStateMapper;
  private final RoomStateMapper roomStateMapper;
  private final VentilationContextFactory ventilationContextFactory;

  public ZoneStateMapper(
      DoorStateMapper doorStateMapper,
      RoomStateMapper roomStateMapper,
      VentilationContextFactory ventilationContextFactory) {
    this.doorStateMapper = doorStateMapper;
    this.roomStateMapper = roomStateMapper;
    this.ventilationContextFactory = ventilationContextFactory;
  }

  public ZoneStateEntity toStateEntity(
      ZoneID id, ZoneState zone, List<DoorStateEntity> doors, List<RoomStateEntity> rooms) {
    VentilationState ventilationState = zone.getVentilationState();
    if (ventilationState.getVentilationSpeedState().isPresent()) {
      return new ZoneStateEntity(
          id,
          zone.getFireState(),
          zone.getSmokePresence(),
          ventilationState.getVentilationSpeedState().get().getDistributionSpeed(),
          ventilationState.getVentilationSpeedState().get().getReturnSpeed(),
          doors,
          rooms,
          zone.getGatherings(),
          zone.getInterventions());
    } else if (ventilationState.getOpenClosedState().isPresent()) {
      return new ZoneStateEntity(
          id,
          zone.getFireState(),
          zone.getSmokePresence(),
          ventilationState.getOpenClosedState().get(),
          doors,
          rooms,
          zone.getGatherings(),
          zone.getInterventions());
    }
    return null;
  }

  public ZoneStateDTO toDTO(ZoneID zoneID, ZoneState zoneState, Map<RoomID, RoomState> roomStates) {
    String ventilationState = null;
    Integer distributionSpeed = null;
    Integer returnSpeed = null;
    if (zoneState.getVentilationState().getOpenClosedState().isPresent()) {
      ventilationState =
          switch (zoneState.getVentilationState().getOpenClosedState().get()) {
            case OPEN -> "ouverte";
            case CLOSE -> "fermée";
            case null, default -> "état inconnue";
          };
    } else if (zoneState.getVentilationState().getVentilationSpeedState().isPresent()) {
      distributionSpeed =
          zoneState.getVentilationState().getVentilationSpeedState().get().getDistributionSpeed();
      returnSpeed =
          zoneState.getVentilationState().getVentilationSpeedState().get().getReturnSpeed();
    }

    int totalOccupation = 0;
    List<String> allIdentified = new ArrayList<>();
    Map<String, Integer> consecutiveAccesses = new HashMap<>();

    for (RoomState room : roomStates.values()) {

      totalOccupation += room.getCurrentOccupancy();

      if (room.getIdentifiedOccupants() != null) {
        allIdentified.addAll(room.getIdentifiedOccupants().stream().map(PersonID::value).toList());
      }

      consecutiveAccesses =
          room.getConsecutiveAccesses().entrySet().stream()
              .collect(Collectors.toMap(entry -> entry.getKey().value(), Map.Entry::getValue));
    }

    return new ZoneStateDTO(
        zoneID.value(),
        FireState.toLabel(zoneState.getFireState()),
        zoneState.getSmokePresence(),
        ventilationState,
        distributionSpeed,
        returnSpeed,
        totalOccupation,
        allIdentified,
        consecutiveAccesses);
  }

  public AggregateZoneState toAggregate(ZoneStateEntity zoneStateEntity) {
    Map<DoorID, DoorState> doorStates = new HashMap<>();
    Map<RoomID, RoomState> roomStates = new HashMap<>();

    zoneStateEntity
        .doors()
        .forEach(
            doorState -> {
              doorStates.put(doorState.id(), doorStateMapper.toState(doorState));
            });
    zoneStateEntity
        .rooms()
        .forEach(
            roomState -> {
              roomStates.put(roomState.id(), roomStateMapper.toState(roomState));
            });

    VentilationSpeedState ventilationSpeedState = null;
    if (zoneStateEntity.ventDistributionSpeed() != null
        && zoneStateEntity.ventReturnSpeed() != null) {
      ventilationSpeedState = new VentilationSpeedState();
      ventilationSpeedState.set(
          zoneStateEntity.ventDistributionSpeed(), zoneStateEntity.ventReturnSpeed());
    }

    VentilationState ventilationState =
        new VentilationState(zoneStateEntity.ventilationState(), ventilationSpeedState);

    ZoneState zoneState =
        new ZoneState(
            zoneStateEntity.fireState(),
            zoneStateEntity.smokePresence(),
            ventilationState,
            new ArrayList<>(zoneStateEntity.gatherings()),
            new ArrayList<>(zoneStateEntity.interventions()));

    return new AggregateZoneState(zoneStateEntity.id(), zoneState, doorStates, roomStates);
  }

  public AggregateZoneState toAggregate(ZoneDTO zoneDTO, String ventilationType) {
    Map<DoorID, DoorState> doorStates = new HashMap<>();
    Map<RoomID, RoomState> roomStates = new HashMap<>();

    zoneDTO
        .doors()
        .forEach(
            door -> {
              doorStates.put(
                  new DoorID(door.id()),
                  new DoorState(
                      OpenClosedState.getState(door.isOpen()),
                      LockedUnlockedState.getState(door.isLocked())));
            });
    zoneDTO
        .rooms()
        .forEach(
            room -> {
              roomStates.put(
                  new RoomID(room.id()),
                  new RoomState(true, 0, new ArrayList<>(), new HashMap<>()));
            });

    OpenClosedState openClosedState = null;
    VentilationSpeedState ventilationSpeedState = null;

    if (ventilationType.equals("CONTROLLABLE_SPEED")) {
      ventilationSpeedState = new VentilationSpeedState();
      if (zoneDTO.type().equals("HAZARDOUS_MATERIAL")) {
        VentilationContext ventilationContext =
            ventilationContextFactory.createNormalStateInHazardousZone();
        ventilationSpeedState.setVentilationContextValue(ventilationContext);
      } else {
        VentilationContext ventilationContext =
            ventilationContextFactory.createNormalStateInRegularZone();
        ventilationSpeedState.setVentilationContextValue(ventilationContext);
      }
    } else openClosedState = OpenClosedState.UNKNOWN;

    VentilationState ventilationState =
        new VentilationState(openClosedState, ventilationSpeedState);

    FireState fireState = FireState.NONE;

    ZoneState zoneState =
        new ZoneState(fireState, false, ventilationState, new ArrayList<>(), new ArrayList<>());

    return new AggregateZoneState(new ZoneID(zoneDTO.id()), zoneState, doorStates, roomStates);
  }
}
