package ca.ulaval.glo4002.application.infrastructure.persistence.state;

import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import java.util.List;
import java.util.Map;

public record RoomStateEntity(
    RoomID id,
    boolean is_electricity_open,
    int currentOccupancy,
    List<PersonID> identifiedOccupants,
    Map<PersonID, Integer> consecutiveAccesses) {}
