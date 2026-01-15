package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.action.FirefighterActionFactory;
import ca.ulaval.glo4002.application.domain.building.*;
import ca.ulaval.glo4002.application.domain.ventilation.Ventilation;
import ca.ulaval.glo4002.application.domain.zone.Zone;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.BuildingDTO;
import java.util.List;

public class BuildingAssembler {
  private final ZoneAssembler zoneAssembler;
  private final VentilationMapper ventilationMapper;
  private final FirefighterActionFactory firefighterActionFactory;
  private final ActionContextFactory actionContextFactory;

  public BuildingAssembler(
      ZoneAssembler zoneAssembler,
      VentilationMapper ventilationMapper,
      FirefighterActionFactory actionFactory,
      ActionContextFactory actionContextFactory) {
    this.zoneAssembler = zoneAssembler;
    this.ventilationMapper = ventilationMapper;
    this.firefighterActionFactory = actionFactory;
    this.actionContextFactory = actionContextFactory;
  }

  public Building toBuilding(
      BuildingDTO buildingDTO, AggregateBuildingState buildingState, ZoneID zoneId) {
    List<Zone> zones =
        buildingDTO.zones().stream()
            .map(
                zoneDTO ->
                    zoneAssembler.toZone(
                        zoneDTO, buildingState.zoneStates().get(new ZoneID(zoneDTO.id()))))
            .toList();
    BuildingID buildingID = new BuildingID(buildingDTO.id());
    BuildingAddress buildingAddress = new BuildingAddress(buildingDTO.postalAddress());
    Ventilation ventilation =
        ventilationMapper.toVentilation(buildingID, buildingDTO.ventilationSystem());

    return new Building(
        buildingID,
        buildingAddress,
        zones,
        ventilation,
        firefighterActionFactory,
        actionContextFactory);
  }
}
