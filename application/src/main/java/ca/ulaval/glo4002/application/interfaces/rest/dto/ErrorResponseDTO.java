package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseDTO(@JsonProperty("message") String message) {
  public static ErrorResponseDTO from(String message) {
    return new ErrorResponseDTO(message);
  }
}
