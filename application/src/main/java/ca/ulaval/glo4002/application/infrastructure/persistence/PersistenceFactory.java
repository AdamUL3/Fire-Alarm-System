package ca.ulaval.glo4002.application.infrastructure.persistence;

import ca.ulaval.glo4002.application.domain.repository.CampusDirectoryRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.infrastructure.external_services.campus_directory.CampusDirectoryService;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.memory.InMemoryStateRepo;
import ca.ulaval.glo4002.application.infrastructure.persistence.sqlite.*;

public class PersistenceFactory {
  public static StatePersistenceRepo create(BuildingStateMapper stateMapper) {
    String persistenceType = System.getProperty("persistence", "memory");

    SQLiteQueryDAO sqLiteQueryDAO = new SQLiteQueryDAO();
    ExternalServicesTableService.createTables();

    if (persistenceType.equals("sqlite")) {
      SQLiteTableService.createTables();
      return new SQLiteStateRepo(sqLiteQueryDAO, stateMapper);
    } else {
      return new InMemoryStateRepo(stateMapper);
    }
  }

  public static CampusDirectoryRepo createCampusDirectoryRepo() {
    ExternalServicesDAO externalServicesDAO = new ExternalServicesDAO();
    return new CampusDirectoryService(externalServicesDAO);
  }
}
