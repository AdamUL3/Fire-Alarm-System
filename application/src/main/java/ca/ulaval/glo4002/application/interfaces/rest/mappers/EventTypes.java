package ca.ulaval.glo4002.application.interfaces.rest.mappers;

public enum EventTypes {
  PREALARM_CONFIRMED("Préalarme confirmée"),
  PREALARM_CANCELLED("Préalarme annulée"),
  PREALARM_EXPIRED("Préalarme expirée"),
  PREALARM("Préalarme d’incendie"),
  SMOKE_PRESENCE("Présence de fumée"),
  FIRE_ALARM("Incendie"),
  FIRE_ALARM_COMPLETED("Incendie terminé"),
  AGENT_SENT("Agent dépêché sur intervention"),
  AGENT_ARRIVED("Agent arrivé sur intervention"),
  AGENT_LEFT("Agent quitte intervention"),
  NO_CARD_ENTRY("Entrée sans carte"),
  EXIT("Sortie de local"),
  GATHERING_START("Rassemblement débuté"),
  GATHERING_END("Rassemblement terminé");

  public final String name;

  EventTypes(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static EventTypes fromName(String name) {
    for (EventTypes type : EventTypes.values()) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("EventType: No event with name " + name);
  }
}
