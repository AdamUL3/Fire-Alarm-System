package ca.ulaval.glo4002.application.interfaces.rest.dto;

import ca.ulaval.glo4002.application.domain.action.ParamDTO;
import java.security.InvalidParameterException;
import java.util.List;

public class SecurityEventDTO {
  public String nom;
  public String heure;
  public String zone;
  public List<ParamDTO> parametres;

  public SecurityEventDTO() {}

  public SecurityEventDTO(String nom, String heure, String zone, List<ParamDTO> parametres) {
    this.nom = nom;
    this.heure = heure;
    this.zone = zone;
    this.parametres = parametres;
  }

  public String getParam(String name) {
    for (ParamDTO parameter : parametres) {
      if (parameter.parametre.equals(name)) {
        return parameter.valeur;
      }
    }
    throw new InvalidParameterException("Paramètre " + name + " introuvable");
  }
}
