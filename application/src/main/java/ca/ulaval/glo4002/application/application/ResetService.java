package ca.ulaval.glo4002.application.application;

import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;

public class ResetService {
  private final StatePersistenceRepo statePersistenceRepo;

  public ResetService(StatePersistenceRepo statePersistenceRepo) {
    this.statePersistenceRepo = statePersistenceRepo;
  }

  public void resetDatabase() {
    statePersistenceRepo.resetDatabase();
  }
}
