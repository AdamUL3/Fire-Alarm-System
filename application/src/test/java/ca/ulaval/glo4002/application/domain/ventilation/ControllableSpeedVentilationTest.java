package ca.ulaval.glo4002.application.domain.ventilation;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.RequestAgentAction;
import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ControllableSpeedVentilationTest {

  private static final boolean HAS_EMERGENCY = true;
  private static final String BUILDING_ID = "BLD-001";
  private static final String ZONE_ID = "Z-001";

  private static final int MEDIAN = 70;
  private static final int PRESSURE = -30;

  private static final int EXPECTED_DISTRIBUTION_AFTER_CONTEXT = 40;
  private static final int EXPECTED_RETURN_AFTER_CONTEXT = 100;

  private static final int INITIAL_DISTRIBUTION = 10;
  private static final int INITIAL_RETURN = 20;

  private static final int CURRENT_DISTRIBUTION = 99;
  private static final int CURRENT_RETURN = 99;

  @Mock VentilationActionFactory ventilationActionFactory;
  @Mock ActionContext actionContext;

  ControllableSpeedVentilation ventilation;
  BuildingID buildingId;
  ZoneID zoneId;
  RequestAgentAction returnedAction;

  @BeforeEach
  void setUp() {
    buildingId = new BuildingID(BUILDING_ID);
    zoneId = new ZoneID(ZONE_ID);
    ventilation = new ControllableSpeedVentilation(buildingId, ventilationActionFactory);
    returnedAction =
        new RequestAgentAction(
            null, null, zoneId, new InterventionID("irrelevant"), AgentPriority.P3);
  }

  @Test
  void givenSpeedState_whenAdjust_thenAdjustVentilationActionWithExpectedSpeeds() {
    VentilationSpeedState speedState = new VentilationSpeedState();
    VentilationState ventilationState = new VentilationState(null, speedState);
    VentilationContext context = new VentilationContext(MEDIAN, PRESSURE, HAS_EMERGENCY);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, EXPECTED_DISTRIBUTION_AFTER_CONTEXT, EXPECTED_RETURN_AFTER_CONTEXT))
        .thenReturn(returnedAction);

    ventilation.adjust(ventilationState, context, actionContext);

    verify(ventilationActionFactory, times(1))
        .createAdjustVentilationSpeed(
            buildingId, zoneId, EXPECTED_DISTRIBUTION_AFTER_CONTEXT, EXPECTED_RETURN_AFTER_CONTEXT);
  }

  @Test
  void givenNoSpeedState_whenAdjust_thenNothing() {
    VentilationState ventilationState = new VentilationState(null, null);
    VentilationContext context = new VentilationContext(MEDIAN, PRESSURE, HAS_EMERGENCY);

    ventilation.adjust(ventilationState, context, actionContext);

    verifyNoInteractions(ventilationActionFactory);
  }

  @Test
  void givenInitialAndCurrentSpeedStates_whenRevert_thenAdjustVentilationActionWithInitialSpeeds() {
    VentilationSpeedState initialSpeed =
        new VentilationSpeedState(INITIAL_DISTRIBUTION, INITIAL_RETURN);
    VentilationState initialState = new VentilationState(null, initialSpeed);
    VentilationSpeedState currentSpeed =
        new VentilationSpeedState(CURRENT_DISTRIBUTION, CURRENT_RETURN);
    VentilationState currentState = new VentilationState(null, currentSpeed);
    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, INITIAL_DISTRIBUTION, INITIAL_RETURN))
        .thenReturn(returnedAction);

    ventilation.revert(currentState, initialState, actionContext);

    verify(ventilationActionFactory, times(1))
        .createAdjustVentilationSpeed(buildingId, zoneId, INITIAL_DISTRIBUTION, INITIAL_RETURN);
  }
}
