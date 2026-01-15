package ca.ulaval.glo4002.application.domain.ventilation;

public class VentilationContextFactory {

  public VentilationContext createProbableEmergencyInHazardousZone(boolean hasEmergency) {
    return new VentilationContext(70, -30, hasEmergency);
  }

  public VentilationContext createProbableEmergencyInRegularZone(boolean hasEmergency) {
    if (hasEmergency) {
      return new VentilationContext(75, -25, hasEmergency);
    }
    return new VentilationContext(75, 25, hasEmergency);
  }

  public VentilationContext createNormalStateInHazardousZone() {
    return new VentilationContext(50, -10, false);
  }

  public VentilationContext createNormalStateInRegularZone() {
    return new VentilationContext(50, 10, false);
  }

  public VentilationContext createProbableEmergencyInUnoccupiedHazardousZone(
      boolean hasEmergency, int doorCount) {
    int pressureVariation = -40;
    if (doorCount >= 5) {
      pressureVariation = -50;
    }
    return new VentilationContext(50, pressureVariation, hasEmergency);
  }
}
