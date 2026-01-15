package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.AlarmState;

public enum AlarmStateResponse {
  RING(true),
  STOP(false);

  private final boolean value;

  AlarmStateResponse(boolean value) {
    this.value = value;
  }

  public static AlarmStateResponse fromDomain(AlarmState alarmState) {
    return switch (alarmState) {
      case RING -> RING;
      case STOP -> STOP;
    };
  }

  public String getValue() {
    return String.valueOf(value);
  }
}
