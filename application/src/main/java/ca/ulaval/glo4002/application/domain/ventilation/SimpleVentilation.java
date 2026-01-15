package ca.ulaval.glo4002.application.domain.ventilation;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;

public class SimpleVentilation extends Ventilation {

  public SimpleVentilation(
      BuildingID buildingId, VentilationActionFactory ventilationActionFactory) {
    super(buildingId, ventilationActionFactory);
  }

  @Override
  public void adjust(
      VentilationState ventilationState,
      VentilationContext ventilationContext,
      ActionContext actionContext) {
    if (ventilationContext.isZoneWithEmergency()
        && !ventilationState.getOpenClosedState().get().isClosed()) {
      ventilationState.setOpenClosedState(OpenClosedState.CLOSE);
      actionContext.addAction(
          ventilationActionFactory.createOpenCloseVentilation(
              buildingId, actionContext.getZoneID(), OpenClosedState.CLOSE));
    } else if (!ventilationState.getOpenClosedState().get().isOpen()) {
      ventilationState.setOpenClosedState(OpenClosedState.OPEN);
      actionContext.addAction(
          ventilationActionFactory.createOpenCloseVentilation(
              buildingId, actionContext.getZoneID(), OpenClosedState.OPEN));
    }
  }

  @Override
  public void revert(
      VentilationState ventilationState,
      VentilationState initialVentilationState,
      ActionContext actionContext) {
    if (initialVentilationState.getOpenClosedState().isPresent()) {
      OpenClosedState state = initialVentilationState.getOpenClosedState().get();
      if (state == OpenClosedState.UNKNOWN) {
        ventilationState.setOpenClosedState(
            OpenClosedState.getReverseState(ventilationState.getOpenClosedState().get()));
        actionContext.addAction(
            ventilationActionFactory.createOpenCloseVentilation(
                buildingId,
                actionContext.getZoneID(),
                ventilationState.getOpenClosedState().get()));
      } else if (!ventilationState.getOpenClosedState().get().equals(state)) {
        ventilationState.setOpenClosedState(initialVentilationState.getOpenClosedState().get());
        actionContext.addAction(
            ventilationActionFactory.createOpenCloseVentilation(
                buildingId,
                actionContext.getZoneID(),
                ventilationState.getOpenClosedState().get()));
      }
    }
  }
}
