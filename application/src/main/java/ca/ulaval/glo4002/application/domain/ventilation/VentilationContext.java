package ca.ulaval.glo4002.application.domain.ventilation;

public class VentilationContext {

  private final int medianSpeed;
  private final int pressureVariation;
  private final boolean hasEmergency;

  public VentilationContext(int medianSpeed, int pressureVariation, boolean hasEmergency) {
    this.medianSpeed = medianSpeed;
    this.pressureVariation = pressureVariation;
    this.hasEmergency = hasEmergency;
  }

  public int getMedianSpeed() {
    return medianSpeed;
  }

  public int getPressureVariation() {
    return pressureVariation;
  }

  public boolean isZoneWithEmergency() {
    return hasEmergency;
  }
}
