package ca.ulaval.glo4002.application.domain.ventilation;

public class VentilationSpeedState {
  protected Integer distributionSpeed;
  protected Integer returnSpeed;

  public VentilationSpeedState() {}

  public VentilationSpeedState(Integer distributionSpeed, Integer returnSpeed) {
    this.distributionSpeed = distributionSpeed;
    this.returnSpeed = returnSpeed;
  }

  public Integer getDistributionSpeed() {
    return distributionSpeed;
  }

  public Integer getReturnSpeed() {
    return returnSpeed;
  }

  public void set(Integer distributionSpeed, Integer returnSpeed) {
    this.distributionSpeed = distributionSpeed;
    this.returnSpeed = returnSpeed;
  }

  public void setVentilationContextValue(VentilationContext ventilationContext) {
    distributionSpeed =
        calculateDistributionSpeed(
            ventilationContext.getPressureVariation(), ventilationContext.getMedianSpeed());
    returnSpeed =
        calculateReturnSpeed(
            ventilationContext.getPressureVariation(), ventilationContext.getMedianSpeed());
  }

  protected Integer calculateDistributionSpeed(int pressureVariation, int speedMedian) {
    return speedMedian + pressureVariation;
  }

  protected Integer calculateReturnSpeed(int pressureVariation, int speedMedian) {
    return speedMedian - pressureVariation;
  }
}
