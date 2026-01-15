package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

public class PersistenceException extends RuntimeException {
  public PersistenceException(String message) {
    super(message);
  }
}
