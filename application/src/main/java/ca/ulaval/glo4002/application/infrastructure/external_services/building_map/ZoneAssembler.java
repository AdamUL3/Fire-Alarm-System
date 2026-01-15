package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.action.AgentActionFactory;
import ca.ulaval.glo4002.application.domain.action.AlarmActionFactory;
import ca.ulaval.glo4002.application.domain.door.Door;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.door.DoorState;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.zone.*;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionFactory;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.ZoneDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZoneAssembler {
  private final RoomAssembler roomAssembler;
  private final DoorAssembler doorAssembler;
  private final AlarmActionFactory alarmActionFactory;
  private final AgentActionFactory agentActionFactory;
  private final GatheringFactory gatheringFactory;
  private final VentilationContextFactory ventilationContextFactory;
  private final InterventionFactory interventionFactory;

  public ZoneAssembler(
      RoomAssembler roomAssembler,
      DoorAssembler doorAssembler,
      AlarmActionFactory alarmActionFactory,
      AgentActionFactory agentActionFactory,
      GatheringFactory gatheringFactory,
      VentilationContextFactory ventilationContextFactory,
      InterventionFactory interventionFactory) {
    this.roomAssembler = roomAssembler;
    this.doorAssembler = doorAssembler;
    this.alarmActionFactory = alarmActionFactory;
    this.agentActionFactory = agentActionFactory;
    this.gatheringFactory = gatheringFactory;
    this.ventilationContextFactory = ventilationContextFactory;
    this.interventionFactory = interventionFactory;
  }

  public Zone toZone(ZoneDTO zoneDTO, AggregateZoneState zoneState) {
    List<Room> rooms =
        zoneDTO.rooms().stream()
            .map(
                roomDTO -> {
                  RoomState roomState =
                      zoneState
                          .roomStates()
                          .getOrDefault(
                              new RoomID(roomDTO.id()),
                              new RoomState(true, 0, new ArrayList<>(), new HashMap<>()));
                  return roomAssembler.toRoom(roomDTO, roomState);
                })
            .toList();

    List<Door> doors =
        zoneDTO.doors().stream()
            .map(
                doorDTO -> {
                  DoorState doorState = zoneState.doorStates().get(new DoorID(doorDTO.id()));
                  return doorAssembler.toDoor(doorDTO, doorState);
                })
            .toList();

    ZoneID zoneID = new ZoneID(zoneDTO.id());

    AgentDispatcher dispatcher = new AgentDispatcher(agentActionFactory, interventionFactory);
    if (zoneDTO.type().equals("HAZARDOUS_MATERIAL")) {
      return new HazardousZone(
          zoneID,
          doors,
          rooms,
          zoneState.zoneState(),
          dispatcher,
          alarmActionFactory,
          gatheringFactory,
          ventilationContextFactory);
    }
    return new Zone(
        zoneID,
        doors,
        rooms,
        zoneState.zoneState(),
        dispatcher,
        alarmActionFactory,
        gatheringFactory,
        ventilationContextFactory);
  }
}
