package ca.ulaval.glo4002.application.domain.zone.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class ZoneNotFoundException extends DomainException {
  public ZoneNotFoundException(ZoneID zoneId) {
    super(String.format("Zone with ID '%s' not found", zoneId.value()), "ZONE_NOT_FOUND");
  }
}
