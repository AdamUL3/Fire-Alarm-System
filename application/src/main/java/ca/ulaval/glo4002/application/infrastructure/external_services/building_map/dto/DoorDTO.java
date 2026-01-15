package ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto;

public record DoorDTO(
    String id, String type, boolean isOpen, boolean isLocked, boolean onEvacuationPath) {}
