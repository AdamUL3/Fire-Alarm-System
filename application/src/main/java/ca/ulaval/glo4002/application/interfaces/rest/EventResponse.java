package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.interfaces.rest.mappers.ActionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EventResponse {

  @JsonProperty("actions")
  private final List<ActionDTO> actions;

  public EventResponse(List<ActionDTO> actions) {
    this.actions = actions;
  }
}
