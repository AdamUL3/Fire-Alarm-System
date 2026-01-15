package ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto;

public record RoomDTO(
    String id, String type, boolean supportsElectricClosure, String labSafetyResponsible) {}
