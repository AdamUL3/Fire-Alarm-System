package ca.ulaval.glo4002.application.domain.ventilation;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;

public abstract class Ventilation {

  protected final BuildingID buildingId;
  protected final VentilationActionFactory ventilationActionFactory;

  public Ventilation(BuildingID buildingId, VentilationActionFactory ventilationActionFactory) {
    this.buildingId = buildingId;
    this.ventilationActionFactory = ventilationActionFactory;
  }

  public abstract void adjust(
      VentilationState ventilationState,
      VentilationContext ventilationContext,
      ActionContext actionContext);

  public abstract void revert(
      VentilationState ventilationState,
      VentilationState initialVentilationState,
      ActionContext actionContext);
}
