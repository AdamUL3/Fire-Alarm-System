package ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto;

import java.util.List;

public record ZoneDTO(
    String id, String type, int currentlyHasActivity, List<RoomDTO> rooms, List<DoorDTO> doors) {}
