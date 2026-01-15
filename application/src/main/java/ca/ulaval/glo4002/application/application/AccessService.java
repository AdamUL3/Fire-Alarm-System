package ca.ulaval.glo4002.application.application;

import ca.ulaval.glo4002.application.domain.access.AccessRequest;
import ca.ulaval.glo4002.application.domain.building.Building;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.repository.AccessCardsRepo;
import ca.ulaval.glo4002.application.domain.repository.BuildingMapRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;

public class AccessService {

  private final StatePersistenceRepo statePersistenceRepo;
  private final BuildingMapRepo buildingMapService;
  private final AccessCardsRepo accessCardsRepo;

  public AccessService(
      StatePersistenceRepo statePersistenceRepo,
      BuildingMapRepo buildingMapService,
      AccessCardsRepo accessCardsRepo) {

    this.statePersistenceRepo = statePersistenceRepo;
    this.buildingMapService = buildingMapService;
    this.accessCardsRepo = accessCardsRepo;
  }

  public void handleAccessRequest(AccessRequest accessRequest) {
    Building building = buildingMapService.findByZoneId(accessRequest.getZoneId());
    PersonAccessInformation cardInformation =
        accessCardsRepo.findByCardNumber(accessRequest.getCardId());
    try {
      accessRequest.handleAccess(building, cardInformation);
    } catch (RefusedAccessException e) {
      statePersistenceRepo.saveCurrentState(building);
      throw e;
    }
    statePersistenceRepo.saveCurrentState(building);
  }
}
