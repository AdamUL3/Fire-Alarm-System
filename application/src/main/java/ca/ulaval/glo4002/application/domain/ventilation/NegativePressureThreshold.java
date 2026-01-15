package ca.ulaval.glo4002.application.domain.ventilation;

public enum NegativePressureThreshold {
  EXTREME(-60),
  ;
  private final int extremeThreshold;

  NegativePressureThreshold(int extremeThreshold) {
    this.extremeThreshold = extremeThreshold;
  }

  public int getValue() {
    return extremeThreshold;
  }
}
