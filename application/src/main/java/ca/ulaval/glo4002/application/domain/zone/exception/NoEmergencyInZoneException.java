package ca.ulaval.glo4002.application.domain.zone.exception;

import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;

public class NoEmergencyInZoneException extends DomainException {

  public NoEmergencyInZoneException(ZoneID zoneId) {
    super(String.format("No emergency in zone '%s'", zoneId.value()), "NO_EMERGENCY_IN_ZONE");
  }
}
