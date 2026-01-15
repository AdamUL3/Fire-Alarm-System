package ca.ulaval.glo4002.application.domain.repository;

import ca.ulaval.glo4002.application.domain.access.CardID;
import ca.ulaval.glo4002.application.domain.user.PersonAccessInformation;

public interface AccessCardsRepo {
  PersonAccessInformation findByCardNumber(CardID cardId);
}
