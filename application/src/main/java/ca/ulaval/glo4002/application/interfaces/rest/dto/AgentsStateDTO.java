package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.atomic.AtomicInteger;

public class AgentsStateDTO {
  @JsonProperty("demandés")
  public AtomicInteger numberOfAgents;

  public AgentsStateDTO(AtomicInteger numberOfAgents) {
    this.numberOfAgents = numberOfAgents;
  }
}
