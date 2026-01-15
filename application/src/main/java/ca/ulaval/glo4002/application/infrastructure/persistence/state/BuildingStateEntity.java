package ca.ulaval.glo4002.application.infrastructure.persistence.state;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import java.util.List;

public record BuildingStateEntity(BuildingID id, List<ZoneStateEntity> zones) {}
