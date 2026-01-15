package ca.ulaval.glo4002.application.infrastructure.persistence.state;

import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.zone.FireState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import java.util.List;

public record ZoneStateEntity(
    ZoneID id,
    FireState fireState,
    boolean smokePresence,
    OpenClosedState ventilationState,
    Integer ventDistributionSpeed,
    Integer ventReturnSpeed,
    List<DoorStateEntity> doors,
    List<RoomStateEntity> rooms,
    List<Gathering> gatherings,
    List<Intervention> interventions) {
  public ZoneStateEntity(
      ZoneID id,
      FireState fireState,
      boolean smokePresence,
      OpenClosedState ventilationState,
      List<DoorStateEntity> doors,
      List<RoomStateEntity> rooms,
      List<Gathering> gatherings,
      List<Intervention> interventions) {
    this(
        id,
        fireState,
        smokePresence,
        ventilationState,
        null,
        null,
        doors,
        rooms,
        gatherings,
        interventions);
  }

  public ZoneStateEntity(
      ZoneID id,
      FireState fireState,
      boolean smokePresence,
      Integer ventDistributionSpeed,
      Integer ventReturnSpeed,
      List<DoorStateEntity> doors,
      List<RoomStateEntity> rooms,
      List<Gathering> gatherings,
      List<Intervention> interventions) {
    this(
        id,
        fireState,
        smokePresence,
        null,
        ventDistributionSpeed,
        ventReturnSpeed,
        doors,
        rooms,
        gatherings,
        interventions);
  }
}
