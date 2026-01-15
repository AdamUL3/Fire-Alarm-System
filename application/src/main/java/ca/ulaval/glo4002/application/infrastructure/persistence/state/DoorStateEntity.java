package ca.ulaval.glo4002.application.infrastructure.persistence.state;

import ca.ulaval.glo4002.application.domain.door.DoorID;

public record DoorStateEntity(DoorID id, boolean openClosedState, boolean lockUnlockState) {}
