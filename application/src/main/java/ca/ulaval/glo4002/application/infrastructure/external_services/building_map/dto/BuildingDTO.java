package ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto;

import java.util.List;

public record BuildingDTO(
    String id, String name, String postalAddress, String ventilationSystem, List<ZoneDTO> zones) {}
