package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.event.*;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.interfaces.rest.dto.SecurityEventDTO;

public class SecurityEventMapper {
  public Event toEvent(SecurityEventDTO securityEvent) {
    EventTypes type = EventTypes.fromName(securityEvent.nom);
    DateTime time = new DateTime(securityEvent.heure);
    ZoneID zoneID = new ZoneID(securityEvent.zone);

    return switch (type) {
      case EventTypes.PREALARM -> new FirePreAlarm(time, zoneID);
      case EventTypes.PREALARM_CONFIRMED -> new ConfirmedPreAlarm(time, zoneID);
      case EventTypes.PREALARM_CANCELLED -> new CancelledPreAlarm(time, zoneID);
      case EventTypes.PREALARM_EXPIRED -> new ExpiredPreAlarm(time, zoneID);
      case EventTypes.SMOKE_PRESENCE ->
          new SmokePresence(
              time, zoneID, Integer.parseInt(securityEvent.getParam("concentration")));
      case EventTypes.FIRE_ALARM_COMPLETED -> new FireAlarmCompleted(time, zoneID);
      case EventTypes.AGENT_LEFT ->
          new AgentLeftIntervention(
              time, zoneID, new InterventionID(securityEvent.getParam("noIntervention")));
      case EventTypes.AGENT_SENT ->
          new AgentSentIntervention(
              time, zoneID, new InterventionID(securityEvent.getParam("noIntervention")));
      case EventTypes.AGENT_ARRIVED ->
          new AgentArrivedIntervention(
              time, zoneID, new InterventionID(securityEvent.getParam("noIntervention")));
      case EventTypes.FIRE_ALARM -> new FireAlarm(time, zoneID);
      case EventTypes.NO_CARD_ENTRY ->
          new NoCardEntry(time, zoneID, new RoomID(securityEvent.getParam("roomId")));
      case EventTypes.EXIT ->
          new ExitRoom(time, zoneID, new RoomID(securityEvent.getParam("roomId")));
      case EventTypes.GATHERING_START ->
          new GatheringStart(
              time,
              zoneID,
              new GatheringID(securityEvent.getParam("identifiantRassemblement")),
              Integer.parseInt(securityEvent.getParam("nombrePersonnesPrevues")),
              convertGatheringType(securityEvent.getParam("typeRassemblement")));
      case EventTypes.GATHERING_END ->
          new GatheringEnd(
              time, zoneID, new GatheringID(securityEvent.getParam("identifiantRassemblement")));
      default -> throw new IllegalArgumentException("SecurityEventMapper: Evenement inconnu");
    };
  }

  private GatheringType convertGatheringType(String gatheringType) {
    return switch (gatheringType) {
      case "ACADEMIQUE" -> GatheringType.ACADEMIC;
      case "MANIFESTATION" -> GatheringType.PROTEST;
      case "ACTIVITE-SOCIALE" -> GatheringType.SOCIAL_ACTIVITY;
      case "AUTRE-RISQUEE" -> GatheringType.OTHER_RISK;
      case "AUTRE" -> GatheringType.OTHER;
      default -> throw new IllegalArgumentException("Unknown gathering type: " + gatheringType);
    };
  }
}
