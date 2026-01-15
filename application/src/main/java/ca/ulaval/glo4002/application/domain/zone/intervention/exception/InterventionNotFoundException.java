package ca.ulaval.glo4002.application.domain.zone.intervention.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;

public class InterventionNotFoundException extends DomainException {
  public InterventionNotFoundException(InterventionID interventionID) {
    super(
        String.format("Intervention with ID '%s' not found", interventionID.value()),
        "INTERVENTION_NOT_FOUND");
  }
}
