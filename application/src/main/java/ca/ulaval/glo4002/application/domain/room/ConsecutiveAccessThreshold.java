package ca.ulaval.glo4002.application.domain.room;

public enum ConsecutiveAccessThreshold {
  THRESHOLD(3),
  ;
  private final int threshold;

  ConsecutiveAccessThreshold(int threshold) {
    this.threshold = threshold;
  }

  public int getValue() {
    return threshold;
  }
}
