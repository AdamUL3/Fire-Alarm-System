package ca.ulaval.glo4002.application.domain.building;

import ca.ulaval.glo4002.application.domain.zone.AggregateZoneState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import java.util.Map;

public record AggregateBuildingState(BuildingID id, Map<ZoneID, AggregateZoneState> zoneStates) {}
