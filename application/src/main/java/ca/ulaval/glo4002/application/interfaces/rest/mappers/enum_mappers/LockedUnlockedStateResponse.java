package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;

public enum LockedUnlockedStateResponse {
  UNLOCKED(0),
  LOCKED(1);

  private final int value;

  LockedUnlockedStateResponse(int value) {
    this.value = value;
  }

  public static LockedUnlockedStateResponse fromDomain(LockedUnlockedState lockedUnlockedState) {
    return switch (lockedUnlockedState) {
      case UNLOCKED -> UNLOCKED;
      case LOCKED -> LOCKED;
    };
  }

  public String getValue() {
    return String.valueOf(value);
  }
}
