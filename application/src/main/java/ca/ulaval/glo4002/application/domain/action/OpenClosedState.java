package ca.ulaval.glo4002.application.domain.action;

public enum OpenClosedState {
  CLOSE,
  OPEN,
  UNKNOWN;

  public static OpenClosedState getState(boolean isOpen) {
    return isOpen ? OPEN : CLOSE;
  }

  public boolean isOpen() {
    return this == OPEN;
  }

  public boolean isClosed() {
    return this == CLOSE;
  }

  public static OpenClosedState getReverseState(OpenClosedState state) {
    return switch (state) {
      case CLOSE -> OPEN;
      case OPEN -> CLOSE;
      default -> UNKNOWN;
    };
  }
}
