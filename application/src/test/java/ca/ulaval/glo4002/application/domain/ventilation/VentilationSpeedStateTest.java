package ca.ulaval.glo4002.application.domain.ventilation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentilationSpeedStateTest {

  private static final boolean HAS_EMERGENCY = true;
  private static final int MEDIAN_SPEED = 70;
  private static final int PRESSURE_VARIATION_POSITIVE = 20;

  private static final int EXPECTED_DISTRIBUTION_SPEED = 90;
  private static final int EXPECTED_RETURN_SPEED = 50;

  private VentilationSpeedState ventilationSpeedState;

  @BeforeEach
  void setUp() {
    ventilationSpeedState = new VentilationSpeedState();
  }

  @Test
  void givenPositivePressureAndMedian_whenCalculateDistributionSpeed_thenReturnsSum() {
    int result =
        ventilationSpeedState.calculateDistributionSpeed(PRESSURE_VARIATION_POSITIVE, MEDIAN_SPEED);

    assertEquals(EXPECTED_DISTRIBUTION_SPEED, result);
  }

  @Test
  void givenPositivePressureAndMedian_whenCalculateReturnSpeed_thenReturnsDifference() {
    int result =
        ventilationSpeedState.calculateReturnSpeed(PRESSURE_VARIATION_POSITIVE, MEDIAN_SPEED);

    assertEquals(EXPECTED_RETURN_SPEED, result);
  }

  @Test
  void givenVentilationContext_whenSetVentilationContextValue_thenUpdatesDistributionSpeed() {
    VentilationContext context =
        new VentilationContext(MEDIAN_SPEED, PRESSURE_VARIATION_POSITIVE, HAS_EMERGENCY);

    ventilationSpeedState.setVentilationContextValue(context);

    assertEquals(EXPECTED_DISTRIBUTION_SPEED, ventilationSpeedState.getDistributionSpeed());
  }

  @Test
  void givenVentilationContext_whenSetVentilationContextValue_thenUpdatesReturnSpeed() {
    VentilationContext context =
        new VentilationContext(MEDIAN_SPEED, PRESSURE_VARIATION_POSITIVE, HAS_EMERGENCY);

    ventilationSpeedState.setVentilationContextValue(context);

    assertEquals(EXPECTED_RETURN_SPEED, ventilationSpeedState.getReturnSpeed());
  }

  @Test
  void givenNewDistributionAndReturnSpeeds_whenSet_thenUpdatesInternalState() {
    int newDistribution = 80;
    int newReturn = 60;

    ventilationSpeedState.set(newDistribution, newReturn);

    assertEquals(newDistribution, ventilationSpeedState.getDistributionSpeed());
    assertEquals(newReturn, ventilationSpeedState.getReturnSpeed());
  }
}
