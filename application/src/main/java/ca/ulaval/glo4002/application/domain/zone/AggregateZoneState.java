package ca.ulaval.glo4002.application.domain.zone;

import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.door.DoorState;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.room.RoomState;
import java.util.Map;

public record AggregateZoneState(
    ZoneID id,
    ZoneState zoneState,
    Map<DoorID, DoorState> doorStates,
    Map<RoomID, RoomState> roomStates) {}
