package ca.ulaval.glo4002.application.domain.event;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.Optional;

public record EventContext(
    ZoneID activeZoneId, Optional<AggregateBuildingState> initialBuildingState) {}
