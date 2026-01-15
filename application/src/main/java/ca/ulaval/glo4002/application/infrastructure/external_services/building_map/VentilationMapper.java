package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.ventilation.ControllableSpeedVentilation;
import ca.ulaval.glo4002.application.domain.ventilation.SimpleVentilation;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;

public class VentilationMapper {
  private final VentilationActionFactory ventilationActionFactory;

  public VentilationMapper(VentilationActionFactory ventilationActionFactory) {
    this.ventilationActionFactory = ventilationActionFactory;
  }

  public Ventilation toVentilation(BuildingID buildingID, String ventilationType) {
    return switch (ventilationType) {
      case "SIMPLE" -> new SimpleVentilation(buildingID, ventilationActionFactory);
      case "CONTROLLABLE_SPEED" ->
          new ControllableSpeedVentilation(buildingID, ventilationActionFactory);
      default ->
          throw new IllegalArgumentException("Système de ventilation inconnu:" + ventilationType);
    };
  }
}
