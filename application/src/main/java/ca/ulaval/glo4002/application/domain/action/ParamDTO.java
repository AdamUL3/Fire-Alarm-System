package ca.ulaval.glo4002.application.domain.action;

public class ParamDTO {
  public String parametre;
  public String valeur;

  public ParamDTO() {}

  public ParamDTO(String parametre, String valeur) {
    this.parametre = parametre;
    this.valeur = valeur;
  }
}
