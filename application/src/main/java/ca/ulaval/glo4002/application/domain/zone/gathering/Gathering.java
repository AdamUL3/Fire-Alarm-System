package ca.ulaval.glo4002.application.domain.zone.gathering;

public class Gathering {
  private final GatheringID id;
  private final int expectedPeopleCount;
  private final GatheringType type;

  public Gathering(GatheringID id, int expectedPeopleCount, GatheringType type) {
    this.id = id;
    this.type = type;
    this.expectedPeopleCount = expectedPeopleCount;
  }

  public GatheringType getType() {
    return type;
  }

  public GatheringID getId() {
    return id;
  }

  public int getExpectedPeopleCount() {
    return expectedPeopleCount;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Gathering g) {
      return id.equals(g.id);
    }
    return false;
  }
}
