package ca.ulaval.glo4002.application.domain.ventilation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentilationContextFactoryTest {

  private static final boolean HAS_EMERGENCY = true;

  private static final int VERY_HIGH_SPEED_HAZARDOUS = 70;
  private static final int VERY_LOW_PRESSURE_HAZARDOUS = -30;
  private static final int HIGH_SPEED_REGULAR = 75;
  private static final int MODERATE_PRESSURE_REGULAR = -25;
  private static final int NORMAL_SPEED = 50;
  private static final int SLIGHTLY_LOW_PRESSURE_HAZARDOUS = -10;
  private static final int SLIGHTLY_HIGH_PRESSURE_REGULAR = 10;
  private static final int PRESSURE_UNOCCUPIED_FEW_DOORS = -40;
  private static final int PRESSURE_UNOCCUPIED_MANY_DOORS = -50;
  private static final int DOOR_THRESHOLD_FOR_PRESSURE = 5;

  VentilationContextFactory factory;

  @BeforeEach
  void setUp() {
    factory = new VentilationContextFactory();
  }

  @Test
  void givenEmergency_whenCreateProbableEmergencyInHazardousZone_thenCorrectMedianSpeedInContext() {
    VentilationContext context = factory.createProbableEmergencyInHazardousZone(HAS_EMERGENCY);

    assertEquals(VERY_HIGH_SPEED_HAZARDOUS, context.getMedianSpeed());
  }

  @Test
  void
      givenEmergency_whenCreateProbableEmergencyInHazardousZone_thenCorrectPressureVariationInContext() {
    VentilationContext context = factory.createProbableEmergencyInHazardousZone(HAS_EMERGENCY);

    assertEquals(VERY_LOW_PRESSURE_HAZARDOUS, context.getPressureVariation());
  }

  @Test
  void givenEmergency_whenCreateProbableEmergencyInHazardousZone_thenZoneWithEmergencyIsTrue() {
    VentilationContext context = factory.createProbableEmergencyInHazardousZone(HAS_EMERGENCY);

    assertTrue(context.isZoneWithEmergency());
  }

  @Test
  void givenNoEmergency_whenCreateProbableEmergencyInHazardousZone_thenZoneWithEmergencyIsFalse() {
    VentilationContext context = factory.createProbableEmergencyInHazardousZone(!HAS_EMERGENCY);

    assertFalse(context.isZoneWithEmergency());
  }

  @Test
  void givenEmergency_whenCreateProbableEmergencyInRegularZone_thenCorrectMedianSpeedInContext() {
    VentilationContext context = factory.createProbableEmergencyInRegularZone(HAS_EMERGENCY);

    assertEquals(HIGH_SPEED_REGULAR, context.getMedianSpeed());
  }

  @Test
  void
      givenEmergency_whenCreateProbableEmergencyInRegularZone_thenCorrectPressureVariationInContext() {
    VentilationContext context = factory.createProbableEmergencyInRegularZone(HAS_EMERGENCY);

    assertEquals(MODERATE_PRESSURE_REGULAR, context.getPressureVariation());
  }

  @Test
  void givenEmergency_whenCreateProbableEmergencyInRegularZone_thenZoneWithEmergencyIsTrue() {
    VentilationContext context = factory.createProbableEmergencyInRegularZone(HAS_EMERGENCY);

    assertTrue(context.isZoneWithEmergency());
  }

  @Test
  void givenNormalState_whenCreateNormalStateInHazardousZone_thenCorrectMedianSpeedInContext() {
    VentilationContext context = factory.createNormalStateInHazardousZone();

    assertEquals(NORMAL_SPEED, context.getMedianSpeed());
  }

  @Test
  void
      givenNormalState_whenCreateNormalStateInHazardousZone_thenCorrectPressureVariationInContext() {
    VentilationContext context = factory.createNormalStateInHazardousZone();

    assertEquals(SLIGHTLY_LOW_PRESSURE_HAZARDOUS, context.getPressureVariation());
  }

  @Test
  void givenNormalState_whenCreateNormalStateInHazardousZone_thenZoneWithEmergencyIsFalse() {
    VentilationContext context = factory.createNormalStateInHazardousZone();

    assertFalse(context.isZoneWithEmergency());
  }

  @Test
  void givenNormalState_whenCreateNormalStateInRegularZone_thenCorrectMedianSpeedInContext() {
    VentilationContext context = factory.createNormalStateInRegularZone();

    assertEquals(NORMAL_SPEED, context.getMedianSpeed());
  }

  @Test
  void givenNormalState_whenCreateNormalStateInRegularZone_thenCorrectPressureVariationInContext() {
    VentilationContext context = factory.createNormalStateInRegularZone();

    assertEquals(SLIGHTLY_HIGH_PRESSURE_REGULAR, context.getPressureVariation());
  }

  @Test
  void givenNormalState_whenCreateNormalStateInRegularZone_thenZoneWithEmergencyIsFalse() {
    VentilationContext context = factory.createNormalStateInRegularZone();

    assertFalse(context.isZoneWithEmergency());
  }

  @Test
  void
      givenEmergencyWithFewDoors_whenCreateProbableEmergencyInUnoccupiedHazardousZone_thenCorrectMedianSpeedInContext() {
    VentilationContext context =
        factory.createProbableEmergencyInUnoccupiedHazardousZone(HAS_EMERGENCY, 3);

    assertEquals(NORMAL_SPEED, context.getMedianSpeed());
  }

  @Test
  void
      givenEmergencyWithFewDoors_whenCreateProbableEmergencyInUnoccupiedHazardousZone_thenCorrectPressureVariationInContext() {
    VentilationContext context =
        factory.createProbableEmergencyInUnoccupiedHazardousZone(HAS_EMERGENCY, 3);

    assertEquals(PRESSURE_UNOCCUPIED_FEW_DOORS, context.getPressureVariation());
  }

  @Test
  void
      givenEmergencyWithFewDoors_whenCreateProbableEmergencyInUnoccupiedHazardousZone_thenZoneWithEmergencyIsTrue() {
    VentilationContext context =
        factory.createProbableEmergencyInUnoccupiedHazardousZone(HAS_EMERGENCY, 3);

    assertTrue(context.isZoneWithEmergency());
  }

  @Test
  void
      givenEmergencyWithManyDoors_whenCreateProbableEmergencyInUnoccupiedHazardousZone_thenCorrectPressureVariationInContext() {
    VentilationContext context =
        factory.createProbableEmergencyInUnoccupiedHazardousZone(
            HAS_EMERGENCY, DOOR_THRESHOLD_FOR_PRESSURE);

    assertEquals(PRESSURE_UNOCCUPIED_MANY_DOORS, context.getPressureVariation());
  }

  @Test
  void
      givenNoEmergencyWithManyDoors_whenCreateProbableEmergencyInUnoccupiedHazardousZone_thenZoneWithEmergencyIsFalse() {
    VentilationContext context =
        factory.createProbableEmergencyInUnoccupiedHazardousZone(!HAS_EMERGENCY, 6);

    assertFalse(context.isZoneWithEmergency());
  }
}
