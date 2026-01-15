package ca.ulaval.glo4002.application.infrastructure.external_services.building_map;

import ca.ulaval.glo4002.application.domain.building.AggregateBuildingState;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.building.exception.BuildingNotFoundException;
import ca.ulaval.glo4002.application.domain.repository.BuildingMapRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.exception.ZoneNotFoundException;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.BuildingDTO;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.dto.BuildingMapResponseDTO;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class BuildingMapService implements BuildingMapRepo {
  private final String ROUTE = "https://building-map-27913344265.northamerica-northeast1.run.app";
  private final BuildingAssembler assembler;
  private final BuildingStateMapper stateMapper;
  private final StatePersistenceRepo stateRepo;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public BuildingMapService(
      BuildingAssembler assembler,
      BuildingStateMapper buildingStateMapper,
      StatePersistenceRepo stateRepo) {
    this.assembler = assembler;
    this.stateMapper = buildingStateMapper;
    this.stateRepo = stateRepo;
  }

  public BuildingMapResponseDTO sendBuildingMapRequest() {
    HttpClient client = HttpClient.newHttpClient();

    try {
      HttpRequest request =
          HttpRequest.newBuilder().uri(URI.create(ROUTE + "/building-map/v2")).GET().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return objectMapper.readValue(response.body(), BuildingMapResponseDTO.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Building findByZoneId(ZoneID zoneId) {

    BuildingMapResponseDTO mapResponse = sendBuildingMapRequest();
    BuildingDTO buildingDTO =
        mapResponse.campus().buildings().stream()
            .filter(
                building ->
                    building.zones().stream().anyMatch(zone -> zone.id().equals(zoneId.value())))
            .findFirst()
            .orElseThrow(() -> new ZoneNotFoundException(zoneId));

    BuildingID buildingId = new BuildingID(buildingDTO.id());

    AggregateBuildingState stateToUse =
        stateRepo
            .findCurrentStateByBuildingId(buildingId)
            .orElseGet(
                () ->
                    stateRepo
                        .findInitialStateByBuildingAndZoneId(buildingId, zoneId)
                        .orElseGet(() -> stateMapper.toAggregate(buildingDTO)));

    return assembler.toBuilding(buildingDTO, stateToUse, zoneId);
  }

  @Override
  public AggregateBuildingState findBuildingStateByBuildingId(BuildingID id) {
    BuildingMapResponseDTO mapResponse = sendBuildingMapRequest();
    BuildingDTO buildingDTO =
        mapResponse.campus().buildings().stream()
            .filter(building -> building.id().equals(id.value()))
            .findFirst()
            .orElseThrow(() -> new BuildingNotFoundException(id));

    Optional<AggregateBuildingState> currentBuildingState =
        stateRepo.findCurrentStateByBuildingId(new BuildingID(buildingDTO.id()));

    return currentBuildingState.orElse(stateMapper.toAggregate(buildingDTO));
  }
}
