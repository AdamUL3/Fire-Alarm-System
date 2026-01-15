package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DoorStateDTO {
  public String id;

  @JsonProperty("fermée")
  public boolean isOpen;

  @JsonProperty("verouillée")
  public boolean isLocked;

  public DoorStateDTO(String id, boolean isOpen, boolean isLocked) {
    this.id = id;
    this.isOpen = isOpen;
    this.isLocked = isLocked;
  }
}
