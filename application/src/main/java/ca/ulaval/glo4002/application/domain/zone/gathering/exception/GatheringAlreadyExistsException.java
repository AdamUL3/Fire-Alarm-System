package ca.ulaval.glo4002.application.domain.zone.gathering.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;

public class GatheringAlreadyExistsException extends DomainException {
  public GatheringAlreadyExistsException(GatheringID gatheringID) {
    super(
        String.format("Gathering with ID '%s' already exists", gatheringID.value()),
        "GATHERING_ALREADY_EXISTS");
  }
}
