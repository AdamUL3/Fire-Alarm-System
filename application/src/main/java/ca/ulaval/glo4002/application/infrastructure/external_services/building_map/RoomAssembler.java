package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.repository.CampusDirectoryRepo;
import ca.ulaval.glo4002.application.domain.room.*;
import ca.ulaval.glo4002.application.domain.room.Room;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.zone.AgentDispatcher;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionFactory;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.RoomDTO;
import java.util.HashMap;
import java.util.List;

public class RoomAssembler {
  private final RoomActionFactory roomActionFactory;
  private final AgentActionFactory agentActionFactory;
  private final AlarmActionFactory alarmActionFactory;
  private final InterventionFactory interventionFactory;
  private final CampusDirectoryRepo campusDirectoryRepo;

  public RoomAssembler(
      RoomActionFactory actionFactory,
      AgentActionFactory agentActionFactory,
      AlarmActionFactory alarmActionFactory,
      CampusDirectoryRepo campusDirectoryRepo,
      InterventionFactory interventionFactory) {
    this.roomActionFactory = actionFactory;
    this.agentActionFactory = agentActionFactory;
    this.alarmActionFactory = alarmActionFactory;
    this.interventionFactory = interventionFactory;
    this.campusDirectoryRepo = campusDirectoryRepo;
  }

  public Room toRoom(RoomDTO roomDTO, RoomState roomState) {
    if (roomState != null) return createRoomWithPreviousState(roomDTO, roomState);
    else return createRoomWithoutPreviousState(roomDTO);
  }

  private Room createRoomWithoutPreviousState(RoomDTO roomDTO) {
    RoomID roomID = new RoomID(roomDTO.id());
    RoomState roomState = new RoomState(false, 0, List.of(), new HashMap<>());
    return getRoom(roomDTO, roomID, roomState);
  }

  private Room createRoomWithPreviousState(RoomDTO roomDTO, RoomState roomState) {
    RoomID roomID = new RoomID(roomDTO.id());
    return getRoom(roomDTO, roomID, roomState);
  }

  private Room getRoom(RoomDTO roomDTO, RoomID roomId, RoomState roomState) {
    return switch (roomDTO.type()) {
      case "LABORATORY" ->
          new Laboratory(
              roomId,
              roomState,
              roomActionFactory,
              campusDirectoryRepo.findByPersonID(new PersonID(roomDTO.labSafetyResponsible())));
      case "SERVER_ROOM" -> new ServerRoom(roomId, roomState, roomActionFactory);
      case "GENERAL_LIMITED" -> new GeneralLimitedRoom(roomId, roomState, roomActionFactory);
      case "RISK_LIMITED" ->
          new RiskLimitedRoom(
              roomId,
              roomState,
              roomActionFactory,
              new AgentDispatcher(agentActionFactory, interventionFactory));
      case "HIGH_RISK" ->
          new HighRiskRoom(
              roomId,
              roomState,
              roomActionFactory,
              new AgentDispatcher(agentActionFactory, interventionFactory),
              alarmActionFactory);
      default -> new GeneralPublicRoom(roomId, roomState, roomActionFactory);
    };
  }
}
