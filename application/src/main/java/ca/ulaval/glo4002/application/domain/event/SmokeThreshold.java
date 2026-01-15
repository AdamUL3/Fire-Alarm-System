package ca.ulaval.glo4002.application.domain.event;

public enum SmokeThreshold {
  THRESHOLD_1(20),
  THRESHOLD_2(50),
  THRESHOLD_3(80);

  private final int threshold;

  SmokeThreshold(int threshold) {
    this.threshold = threshold;
  }

  public int getThreshold() {
    return threshold;
  }
}
