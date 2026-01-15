package ca.ulaval.glo4002.application.domain.zone.gathering;

public class GatheringFactory {
  public Gathering createGathering(
      GatheringID gatheringId, int expectedPeopleCount, GatheringType gatheringType) {
    if (expectedPeopleCount < 0) {
      throw new RuntimeException("Nombre de personne négatif");
    }
    return new Gathering(gatheringId, expectedPeopleCount, gatheringType);
  }
}
