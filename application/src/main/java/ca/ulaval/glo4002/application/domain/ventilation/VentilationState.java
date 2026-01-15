package ca.ulaval.glo4002.application.domain.ventilation;

import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import java.util.Optional;

public class VentilationState {
  private OpenClosedState openClosedState;
  private final VentilationSpeedState ventilationSpeedState;

  public VentilationState(
      OpenClosedState openClosedState, VentilationSpeedState ventilationSpeedState) {
    this.openClosedState = openClosedState;
    this.ventilationSpeedState = ventilationSpeedState;
  }

  public Optional<OpenClosedState> getOpenClosedState() {
    return Optional.ofNullable(openClosedState);
  }

  public void setOpenClosedState(OpenClosedState openClosedState) {
    this.openClosedState = openClosedState;
  }

  public Optional<VentilationSpeedState> getVentilationSpeedState() {
    return Optional.ofNullable(ventilationSpeedState);
  }

  public boolean hasExtremePressure() {
    return ventilationSpeedState.getDistributionSpeed() - ventilationSpeedState.getReturnSpeed()
        <= NegativePressureThreshold.EXTREME.getValue();
  }
}
