package ca.ulaval.glo4002.application.domain.user;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.access.CardID;

public class PersonAccessInformation {
  private final CardID cardId;
  private final PersonID personId;
  private final AccessRole accessRole;
  private boolean isLabResponsibleInBuilding;

  public PersonAccessInformation(
      CardID cardId, PersonID personId, AccessRole role, boolean isLabResponsibleInBuilding) {
    this.cardId = cardId;
    this.personId = personId;
    this.accessRole = role;
    this.isLabResponsibleInBuilding = isLabResponsibleInBuilding;
  }

  public void setLabResponsibleInBuilding(boolean labResponsibleInBuilding) {
    isLabResponsibleInBuilding = labResponsibleInBuilding;
  }

  public CardID getCardId() {
    return cardId;
  }

  public PersonID getPersonId() {
    return personId;
  }

  public AccessRole getRole() {
    return accessRole;
  }

  public boolean isLabResponsibleInBuilding() {
    return isLabResponsibleInBuilding;
  }
}
