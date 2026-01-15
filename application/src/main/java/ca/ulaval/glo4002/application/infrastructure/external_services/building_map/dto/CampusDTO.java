package ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto;

import java.util.List;

public record CampusDTO(String id, String name, List<BuildingDTO> buildings) {}
