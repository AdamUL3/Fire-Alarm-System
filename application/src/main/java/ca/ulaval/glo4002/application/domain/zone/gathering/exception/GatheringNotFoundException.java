package ca.ulaval.glo4002.application.domain.zone.gathering.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;

public class GatheringNotFoundException extends DomainException {
  public GatheringNotFoundException(GatheringID gatheringID) {
    super(
        String.format("Gathering with ID '%s' not found", gatheringID.value()),
        "GATHERING_NOT_FOUND");
  }
}
