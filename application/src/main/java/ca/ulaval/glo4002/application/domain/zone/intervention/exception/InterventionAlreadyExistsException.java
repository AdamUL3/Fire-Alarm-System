package ca.ulaval.glo4002.application.domain.zone.intervention.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;

public class InterventionAlreadyExistsException extends DomainException {
  public InterventionAlreadyExistsException(InterventionID interventionID) {
    super(
        String.format("Intervention with ID '%s' already exists", interventionID.value()),
        "INTERVENTION_NOT_FOUND");
  }
}
