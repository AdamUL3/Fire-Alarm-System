package ca.ulaval.glo4002.application.domain.building.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;

public class RefusedAccessException extends DomainException {
  public RefusedAccessException() {
    super(null, null);
  }
}
