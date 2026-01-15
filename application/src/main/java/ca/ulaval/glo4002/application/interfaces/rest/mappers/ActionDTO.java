package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.action.ParamDTO;
import java.util.List;

public class ActionDTO {
  public String nom;
  public List<ParamDTO> params;

  public ActionDTO(String nom, List<ParamDTO> params) {
    this.nom = nom;
    this.params = params;
  }
}
