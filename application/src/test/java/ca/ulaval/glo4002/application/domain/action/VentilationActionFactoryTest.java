package ca.ulaval.glo4002.application.domain.action;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentilationActionFactoryTest {
  private VentilationActionFactory ventilationActionFactory;

  @Mock private BuildingID buildingId;
  @Mock private ZoneID zoneId;

  private static final int DISTRIBUTION_SPEED = 42;
  private static final int RETURN_QUANTITY = 7;

  @BeforeEach
  public void setUp() {
    ventilationActionFactory = new VentilationActionFactory();
  }

  @Test
  void givenBuilding_whenCreateAdjustVentilationSpeed_thenBuildingIsSet() {
    Action result =
        ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, DISTRIBUTION_SPEED, RETURN_QUANTITY);
    AdjustVentilationSpeedAction returnedAction = (AdjustVentilationSpeedAction) result;
    assertSame(buildingId, returnedAction.getBuildingId());
  }

  @Test
  void givenZone_whenCreateAdjustVentilationSpeed_thenZoneIsSet() {
    Action result =
        ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, DISTRIBUTION_SPEED, RETURN_QUANTITY);
    AdjustVentilationSpeedAction returnedAction = (AdjustVentilationSpeedAction) result;
    assertSame(zoneId, returnedAction.getZoneId());
  }

  @Test
  void givenDistributionSpeed_whenCreateAdjustVentilationSpeed_thenDistributionSpeedIsSet() {
    Action result =
        ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, DISTRIBUTION_SPEED, RETURN_QUANTITY);
    AdjustVentilationSpeedAction returnedAction = (AdjustVentilationSpeedAction) result;
    assertEquals(DISTRIBUTION_SPEED, returnedAction.getDistributionSpeed());
  }

  @Test
  void givenReturnQuantity_whenCreateAdjustVentilationSpeed_thenReturnQuantityIsSet() {
    Action result =
        ventilationActionFactory.createAdjustVentilationSpeed(
            buildingId, zoneId, DISTRIBUTION_SPEED, RETURN_QUANTITY);
    AdjustVentilationSpeedAction returnedAction = (AdjustVentilationSpeedAction) result;
    assertEquals(RETURN_QUANTITY, returnedAction.getReturnQuantity());
  }

  @Test
  void givenState_whenCreateOpenCloseVentilation_thenStateIsSet() {
    Action result =
        ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.OPEN);
    OpenCloseVentilationAction returnedAction = (OpenCloseVentilationAction) result;
    assertSame(OpenClosedState.OPEN, returnedAction.getVentilationState());
  }
}
