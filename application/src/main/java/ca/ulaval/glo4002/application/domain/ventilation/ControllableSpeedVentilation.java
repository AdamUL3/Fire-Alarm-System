package ca.ulaval.glo4002.application.domain.ventilation;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;

public class ControllableSpeedVentilation extends Ventilation {

  public ControllableSpeedVentilation(
      BuildingID buildingId, VentilationActionFactory ventilationActionFactory) {
    super(buildingId, ventilationActionFactory);
  }

  @Override
  public void adjust(
      VentilationState ventilationState,
      VentilationContext ventilationContext,
      ActionContext actionContext) {

    if (ventilationState.getVentilationSpeedState().isPresent()) {
      VentilationSpeedState ventilationSpeedState =
          ventilationState.getVentilationSpeedState().get();

      ventilationSpeedState.setVentilationContextValue(ventilationContext);

      actionContext.addAction(
          ventilationActionFactory.createAdjustVentilationSpeed(
              buildingId,
              actionContext.getZoneID(),
              ventilationSpeedState.getDistributionSpeed(),
              ventilationSpeedState.getReturnSpeed()));
    }
  }

  @Override
  public void revert(
      VentilationState ventilationState,
      VentilationState initialVentilationState,
      ActionContext actionContext) {
    VentilationSpeedState initialVentilationSpeedState =
        initialVentilationState.getVentilationSpeedState().get();
    VentilationSpeedState ventilationSpeedState = ventilationState.getVentilationSpeedState().get();

    ventilationSpeedState.set(
        initialVentilationSpeedState.getDistributionSpeed(),
        initialVentilationSpeedState.getReturnSpeed());

    actionContext.addAction(
        ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId,
            actionContext.getZoneID(),
            initialVentilationSpeedState.getDistributionSpeed(),
            initialVentilationSpeedState.getReturnSpeed()));
  }
}
