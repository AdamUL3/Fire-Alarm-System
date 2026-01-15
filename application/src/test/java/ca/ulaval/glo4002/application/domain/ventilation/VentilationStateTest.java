package ca.ulaval.glo4002.application.domain.ventilation;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentilationStateTest {

  private static final int NORMAL_DISTRIBUTION_SPEED = 70;
  private static final int NORMAL_RETURN_SPEED = 60;
  private static final int EXTREME_THRESHOLD = NegativePressureThreshold.EXTREME.getValue();

  private VentilationState ventilationState;

  @BeforeEach
  void setUp() {
    VentilationSpeedState ventilationSpeedState =
        new VentilationSpeedState(NORMAL_DISTRIBUTION_SPEED, NORMAL_RETURN_SPEED);
    ventilationState = new VentilationState(OpenClosedState.OPEN, ventilationSpeedState);
  }

  @Test
  void givenNewOpenClosedState_whenSetOpenClosedState_thenUpdatesState() {
    ventilationState.setOpenClosedState(OpenClosedState.CLOSE);

    assertEquals(OpenClosedState.CLOSE, ventilationState.getOpenClosedState().get());
  }

  @Test
  void givenNormalPressureDifference_whenHasExtremePressure_thenReturnsFalse() {
    boolean result = ventilationState.hasExtremePressure();

    assertFalse(result);
  }

  @Test
  void givenExtremeNegativePressureDifference_whenHasExtremePressure_thenReturnsTrue() {
    VentilationSpeedState extremeState =
        new VentilationSpeedState(NORMAL_RETURN_SPEED + EXTREME_THRESHOLD, NORMAL_RETURN_SPEED);
    VentilationState stateWithExtremePressure =
        new VentilationState(OpenClosedState.OPEN, extremeState);

    boolean result = stateWithExtremePressure.hasExtremePressure();

    assertTrue(result);
  }
}
