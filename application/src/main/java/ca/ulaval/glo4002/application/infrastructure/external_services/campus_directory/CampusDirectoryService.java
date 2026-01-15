package ca.ulaval.glo4002.application.infrastructure.external_services.campus_directory;

import ca.ulaval.glo4002.application.domain.repository.CampusDirectoryRepo;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.user.PersonInformation;
import ca.ulaval.glo4002.application.infrastructure.persistence.sqlite.ExternalServicesDAO;

public class CampusDirectoryService implements CampusDirectoryRepo {
  private final ExternalServicesDAO externalServicesDAO;

  public CampusDirectoryService(ExternalServicesDAO externalServicesDAO) {
    this.externalServicesDAO = externalServicesDAO;
  }

  @Override
  public PersonInformation findByPersonID(PersonID id) {
    return externalServicesDAO.findByPersonId(id);
  }
}
