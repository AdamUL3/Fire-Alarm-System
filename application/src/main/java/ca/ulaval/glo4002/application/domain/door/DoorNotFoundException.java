package ca.ulaval.glo4002.application.domain.door;

import ca.ulaval.glo4002.application.domain.exception.DomainException;

public class DoorNotFoundException extends DomainException {
  public DoorNotFoundException(DoorID doorID) {
    super(String.format("Door with ID '%s' not found", doorID.value()), "DOOR_NOT_FOUND");
  }
}
