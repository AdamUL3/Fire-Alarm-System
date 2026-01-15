package ca.ulaval.glo4002.application.domain.ventilation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.domain.action.ActionContext;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.action.VentilationActionFactory;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleVentilationTest {

  private static final String BUILDING_ID = "BLD-001";
  private static final String ZONE_ID = "Z-001";

  @Mock private VentilationActionFactory ventilationActionFactory;
  @Mock private ActionContext actionContext;
  @Mock private Action returnedAction;

  private SimpleVentilation ventilation;
  private BuildingID buildingId;
  private ZoneID zoneId;

  @BeforeEach
  void setUp() {
    buildingId = new BuildingID(BUILDING_ID);
    zoneId = new ZoneID(ZONE_ID);
    ventilation = new SimpleVentilation(buildingId, ventilationActionFactory);
  }

  @Test
  void givenZoneWithEmergencyAndVentOpen_whenAdjust_thenSetVentStateToClose() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.OPEN, null);
    VentilationContext context = new VentilationContext(0, 0, true);

    ventilation.adjust(ventilationState, context, actionContext);

    assertEquals(OpenClosedState.CLOSE, ventilationState.getOpenClosedState().get());
  }

  @Test
  void givenZoneWithEmergencyAndVentOpen_whenAdjust_thenCreateCloseVentilationAction() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.OPEN, null);
    VentilationContext context = new VentilationContext(0, 0, true);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    ventilation.adjust(ventilationState, context, actionContext);

    verify(ventilationActionFactory)
        .createOpenCloseVentilation(buildingId, zoneId, OpenClosedState.CLOSE);
  }

  @Test
  void givenZoneWithEmergencyAndVentOpen_whenAdjust_thenAddCloseActionToContext() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.OPEN, null);
    VentilationContext context = new VentilationContext(0, 0, true);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    ventilation.adjust(ventilationState, context, actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenVentClosedAndNoEmergency_whenAdjust_thenSetVentStateToOpen() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.CLOSE, null);
    VentilationContext context = new VentilationContext(0, 0, false);

    ventilation.adjust(ventilationState, context, actionContext);

    assertEquals(OpenClosedState.OPEN, ventilationState.getOpenClosedState().get());
  }

  @Test
  void givenVentClosedAndNoEmergency_whenAdjust_thenCreateOpenVentilationAction() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.CLOSE, null);
    VentilationContext context = new VentilationContext(0, 0, false);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.OPEN))
        .thenReturn(returnedAction);

    ventilation.adjust(ventilationState, context, actionContext);

    verify(ventilationActionFactory)
        .createOpenCloseVentilation(buildingId, zoneId, OpenClosedState.OPEN);
  }

  @Test
  void givenVentClosedAndNoEmergency_whenAdjust_thenAddOpenActionToContext() {
    VentilationState ventilationState = new VentilationState(OpenClosedState.CLOSE, null);
    VentilationContext context = new VentilationContext(0, 0, false);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.OPEN))
        .thenReturn(returnedAction);

    ventilation.adjust(ventilationState, context, actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenInitialStateUnknown_whenRevert_thenReverseVentState() {
    VentilationState initial = new VentilationState(OpenClosedState.UNKNOWN, null);
    VentilationState current = new VentilationState(OpenClosedState.OPEN, null);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    ventilation.revert(current, initial, actionContext);

    assertEquals(OpenClosedState.CLOSE, current.getOpenClosedState().get());
  }

  @Test
  void givenInitialStateUnknown_whenRevert_thenCreateActionWithReverseState() {
    VentilationState initial = new VentilationState(OpenClosedState.UNKNOWN, null);
    VentilationState current = new VentilationState(OpenClosedState.OPEN, null);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    ventilation.revert(current, initial, actionContext);

    verify(ventilationActionFactory)
        .createOpenCloseVentilation(buildingId, zoneId, OpenClosedState.CLOSE);
  }

  @Test
  void givenInitialStateUnknown_whenRevert_thenAddActionToContext() {
    VentilationState initial = new VentilationState(OpenClosedState.UNKNOWN, null);
    VentilationState current = new VentilationState(OpenClosedState.OPEN, null);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, OpenClosedState.CLOSE))
        .thenReturn(returnedAction);

    ventilation.revert(current, initial, actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenDifferentInitialState_whenRevert_thenSetVentStateToInitial() {
    VentilationState initial = new VentilationState(OpenClosedState.OPEN, null);
    VentilationState current = new VentilationState(OpenClosedState.CLOSE, null);

    ventilation.revert(current, initial, actionContext);

    assertEquals(initial.getOpenClosedState().get(), current.getOpenClosedState().get());
  }

  @Test
  void givenDifferentInitialState_whenRevert_thenCreateActionWithInitialState() {
    VentilationState initial = new VentilationState(OpenClosedState.OPEN, null);
    VentilationState current = new VentilationState(OpenClosedState.CLOSE, null);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, initial.getOpenClosedState().get()))
        .thenReturn(returnedAction);

    ventilation.revert(current, initial, actionContext);

    verify(ventilationActionFactory)
        .createOpenCloseVentilation(buildingId, zoneId, initial.getOpenClosedState().get());
  }

  @Test
  void givenDifferentInitialState_whenRevert_thenAddActionToContext() {
    VentilationState initial = new VentilationState(OpenClosedState.OPEN, null);
    VentilationState current = new VentilationState(OpenClosedState.CLOSE, null);

    when(actionContext.getZoneID()).thenReturn(zoneId);
    when(ventilationActionFactory.createOpenCloseVentilation(
            buildingId, zoneId, initial.getOpenClosedState().get()))
        .thenReturn(returnedAction);

    ventilation.revert(current, initial, actionContext);

    verify(actionContext).addAction(returnedAction);
  }

  @Test
  void givenSameInitialState_whenRevert_thenDoNothing() {
    VentilationState initial = new VentilationState(OpenClosedState.OPEN, null);
    VentilationState current = new VentilationState(OpenClosedState.OPEN, null);

    ventilation.revert(current, initial, actionContext);

    verifyNoInteractions(ventilationActionFactory);
  }
}
