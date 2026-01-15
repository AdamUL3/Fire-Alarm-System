package ca.ulaval.glo4002.application.domain.action;

public enum LockedUnlockedState {
  UNLOCKED,
  LOCKED;

  public boolean isLocked() {
    return this == LOCKED;
  }

  public static LockedUnlockedState getState(boolean isLocked) {
    if (isLocked) {
      return LOCKED;
    }
    return UNLOCKED;
  }
}
