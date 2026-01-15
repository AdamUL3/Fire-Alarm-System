package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BuildingStateDTO {
  public String id;
  public AgentsStateDTO agents;
  public List<ZoneStateDTO> zones;

  @JsonProperty("portes")
  public List<DoorStateDTO> doors;

  public BuildingStateDTO(
      String id, AgentsStateDTO agents, List<ZoneStateDTO> zones, List<DoorStateDTO> doors) {
    this.id = id;
    this.agents = agents;
    this.zones = zones;
    this.doors = doors;
  }
}
