package ca.ulaval.glo4002.application.domain.zone;

public enum FireState {
  NONE,
  PROBABLE,
  CONFIRMED;

  public boolean hasEmergency() {
    return this == PROBABLE || this == CONFIRMED;
  }

  public static FireState fromLabel(String label) {
    switch (label) {
      case "NONE" -> {
        return FireState.NONE;
      }
      case "PROBABLE" -> {
        return FireState.PROBABLE;
      }
      case "CONFIRMED" -> {
        return FireState.CONFIRMED;
      }
    }
    return FireState.NONE;
  }

  public static String toLabel(FireState fireState) {
    return switch (fireState) {
      case NONE -> "AUCUN";
      case PROBABLE -> "PROBABLE";
      case CONFIRMED -> "CONFIRMÉ";
    };
  }
}
