package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.action.LockedUnlockedState;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;

public class DoorState {
  private OpenClosedState openClosedState;
  private LockedUnlockedState lockedUnlockedState;

  public DoorState(OpenClosedState openClosedState, LockedUnlockedState lockedUnlockedState) {
    this.openClosedState = openClosedState;
    this.lockedUnlockedState = lockedUnlockedState;
  }

  public OpenClosedState getOpenClosedState() {
    return openClosedState;
  }

  public void setOpenClosedState(OpenClosedState openClosedState) {
    this.openClosedState = openClosedState;
  }

  public LockedUnlockedState getLockUnlockState() {
    return lockedUnlockedState;
  }

  public void setLockUnlockState(LockedUnlockedState lockedUnlockedState) {
    this.lockedUnlockedState = lockedUnlockedState;
  }
}
