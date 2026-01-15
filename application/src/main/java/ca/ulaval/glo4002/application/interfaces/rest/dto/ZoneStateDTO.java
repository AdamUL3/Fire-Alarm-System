package ca.ulaval.glo4002.application.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZoneStateDTO {
  public String id;

  @JsonProperty("etat_incendie")
  public String fireState;

  @JsonProperty("présence_de_fumée")
  public boolean smokePresence;

  @JsonProperty("ventilation")
  public String ventilationState;

  @JsonProperty("ventilation_vitesse_distribution")
  public Integer ventDistributionSpeed;

  @JsonProperty("ventilation_vitesse_retour")
  public Integer ventReturnSpeed;

  @JsonProperty("occupation_actuelle")
  public int currentOccupation;

  @JsonProperty("occupants_identifiés")
  public List<String> identifiedOccupants;

  @JsonProperty("acces_consecutifs_par_personne")
  public Map<String, Integer> consecutiveAccesses;

  public ZoneStateDTO(
      String id,
      String fireState,
      boolean smokePresence,
      String ventilationState,
      Integer ventDistributionSpeed,
      Integer ventReturnSpeed,
      Integer currentOccupation,
      List<String> identifiedOccupants,
      Map<String, Integer> consecutiveAccesses) {
    this.id = id;
    this.fireState = fireState;
    this.smokePresence = smokePresence;
    this.ventilationState = ventilationState;
    this.ventDistributionSpeed = ventDistributionSpeed;
    this.ventReturnSpeed = ventReturnSpeed;
    this.currentOccupation = currentOccupation;
    this.identifiedOccupants = identifiedOccupants;
    this.consecutiveAccesses = consecutiveAccesses;
  }
}
