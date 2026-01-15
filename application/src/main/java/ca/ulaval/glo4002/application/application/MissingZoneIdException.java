package ca.ulaval.glo4002.application.application;

public class MissingZoneIdException extends RuntimeException {
  public MissingZoneIdException() {
    super("zone_id is required");
  }
}
