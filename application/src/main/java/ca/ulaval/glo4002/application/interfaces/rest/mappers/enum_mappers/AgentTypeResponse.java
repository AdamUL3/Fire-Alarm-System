package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.AgentType;

public enum AgentTypeResponse {
  PATROUILLEUR;

  public static AgentTypeResponse fromDomain(AgentType agentType) {
    return switch (agentType) {
      case PATROL -> PATROUILLEUR;
    };
  }

  public String getValue() {
    return name();
  }
}
