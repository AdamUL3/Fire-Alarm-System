package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.AgentPriority;

public enum AgentPriorityResponse {
  P1,
  P2,
  P3,
  P4,
  P5;

  public static AgentPriorityResponse fromDomain(AgentPriority agentPriority) {
    return switch (agentPriority) {
      case P1 -> P1;
      case P2 -> P2;
      case P3 -> P3;
      case P4 -> P4;
      case P5 -> P5;
    };
  }

  public String getValue() {
    return name();
  }
}
