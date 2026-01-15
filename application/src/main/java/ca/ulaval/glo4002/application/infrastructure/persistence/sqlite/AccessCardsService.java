package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import ca.ulaval.glo4002.application.domain.access.CardID;
import ca.ulaval.glo4002.application.domain.repository.AccessCardsRepo;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;

public class AccessCardsService implements AccessCardsRepo {
  private final ExternalServicesDAO externalServicesDAO;

  public AccessCardsService(ExternalServicesDAO externalServicesDAO) {
    this.externalServicesDAO = externalServicesDAO;
  }

  @Override
  public PersonAccessInformation findByCardNumber(CardID cardId) {
    return externalServicesDAO.findByCardNumber(cardId);
  }
}
