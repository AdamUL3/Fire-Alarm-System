package ca.ulaval.glo4002.application.domain.action;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.building.BuildingAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirefighterActionFactoryTest {
  private FirefighterActionFactory firefighterActionFactory;

  @Mock private BuildingAddress buildingAddress;

  @BeforeEach
  void setUp() {
    firefighterActionFactory = new FirefighterActionFactory();
  }

  @Test
  void givenAddress_whenCreateCallFirefighters_thenBuildingAddressIsSet() {
    Action result =
        firefighterActionFactory.createCallFirefighters(buildingAddress, CallReasonType.FIRE);
    CallFirefightersAction returnedAction = (CallFirefightersAction) result;
    assertSame(buildingAddress, returnedAction.getBuildingAddress());
  }

  @Test
  void givenReasonType_whenCreateCallFirefighters_thenReasonTypeIsSet() {
    Action result =
        firefighterActionFactory.createCallFirefighters(buildingAddress, CallReasonType.FIRE);
    CallFirefightersAction returnedAction = (CallFirefightersAction) result;
    assertSame(CallReasonType.FIRE, returnedAction.getCallReason());
  }
}
