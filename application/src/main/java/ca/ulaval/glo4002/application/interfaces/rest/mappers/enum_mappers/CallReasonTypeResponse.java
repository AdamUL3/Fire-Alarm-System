package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.CallReasonType;

public enum CallReasonTypeResponse {
  FIRE,
  SMOKE,
  CHECK,
  SIMULATION;

  public static CallReasonTypeResponse fromDomain(CallReasonType callReasonType) {
    return switch (callReasonType) {
      case FIRE -> FIRE;
      case SMOKE -> SMOKE;
      case CHECK -> CHECK;
      case SIMULATION -> SIMULATION;
    };
  }

  public String getValue() {
    return name();
  }
}
