package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.OpenClosedState;

public enum OpenClosedStateResponse {
  CLOSE(0),
  OPEN(1);

  private final int value;

  OpenClosedStateResponse(int value) {
    this.value = value;
  }

  public static OpenClosedStateResponse fromDomain(OpenClosedState openClosedState) {
    return switch (openClosedState) {
      case CLOSE -> CLOSE;
      case OPEN -> OPEN;
      case UNKNOWN -> null;
    };
  }

  public String getValue() {
    return String.valueOf(value);
  }
}
