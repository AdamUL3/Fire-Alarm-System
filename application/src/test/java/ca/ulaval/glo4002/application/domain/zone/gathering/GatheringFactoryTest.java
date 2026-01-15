package ca.ulaval.glo4002.application.domain.zone.gathering;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GatheringFactoryTest {

  private GatheringFactory gatheringFactory;

  private static GatheringID GatheringId;

  private static final int NORMAL_COUNT = 25;
  private static final int INVALID_COUNT = -3;

  @BeforeEach
  void setUp() {
    GatheringId = new GatheringID("ACADEMIC_1");
    gatheringFactory = new GatheringFactory();
  }

  @Test
  void givenId_whenCreateGathering_thenCorrectIdUsed() {
    Gathering gathering =
        gatheringFactory.createGathering(GatheringId, NORMAL_COUNT, GatheringType.ACADEMIC);

    assertEquals(GatheringId, gathering.getId());
  }

  @Test
  void givenValidExpectedPeopleCount_whenCreateGathering_thenCorrectExpectedPeopleCountUsed() {
    Gathering gathering =
        gatheringFactory.createGathering(GatheringId, NORMAL_COUNT, GatheringType.ACADEMIC);

    assertEquals(NORMAL_COUNT, gathering.getExpectedPeopleCount());
  }

  @Test
  void givenInvalidExpectedPeopleCount_whenCreateGathering_thenThrowsRuntimeException() {
    assertThrows(
        RuntimeException.class,
        () -> gatheringFactory.createGathering(GatheringId, INVALID_COUNT, GatheringType.ACADEMIC));
  }

  @Test
  void givenGatheringType_whenCreateGathering_thenCorrectTypeUsed() {
    Gathering gathering =
        gatheringFactory.createGathering(GatheringId, NORMAL_COUNT, GatheringType.ACADEMIC);

    assertEquals(GatheringType.ACADEMIC, gathering.getType());
  }
}
