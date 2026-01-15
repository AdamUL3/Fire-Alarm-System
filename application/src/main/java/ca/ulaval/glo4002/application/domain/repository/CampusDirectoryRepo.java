package ca.ulaval.glo4002.application.domain.repository;

import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.user.PersonInformation;

public interface CampusDirectoryRepo {
  PersonInformation findByPersonID(PersonID id);
}
