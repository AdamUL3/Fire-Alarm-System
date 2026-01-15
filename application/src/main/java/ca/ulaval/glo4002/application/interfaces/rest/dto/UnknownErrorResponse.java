package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public record UnknownErrorResponse(String message) {
  @JsonCreator
  public UnknownErrorResponse {}
}
