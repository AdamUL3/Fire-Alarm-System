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
class AlarmActionFactoryTest {
  private AlarmActionFactory alarmActionFactory;

  @Mock private BuildingID buildingId;
  @Mock private ZoneID zoneId;

  @BeforeEach
  public void setUp() {
    alarmActionFactory = new AlarmActionFactory();
  }

  @Test
  void givenRingState_whenCreateActivateFireAlarm_thenRingStateIsSet() {
    Action result = alarmActionFactory.createActivateFireAlarm(buildingId, zoneId, AlarmState.RING);
    ActivateFireAlarmAction returnedAction = (ActivateFireAlarmAction) result;
    assertSame(AlarmState.RING, returnedAction.getAlarmState());
  }
}
