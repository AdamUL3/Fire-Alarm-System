package ca.ulaval.glo4002.application.interfaces.rest.mappers;

public enum ActionName {
  DEMANDER_AGENT("DemanderAgent"),
  REGLER_VITESSE_VENTILATION("ReglerVitesseVentilation"),
  OUVRIR_FERMER_VENTILATION("OuvrirFermerVentilation"),
  ACTIVER_ALARME_INCENDIE("ActiverAlarmeIncendie"),
  APPELER_POMPIERS("AppelerPompiers"),
  ENVOYER_SMS_MESSAGE_PREDEFINI("EnvoyerSMSMessagePredefini"),
  OUVRIR_FERMER_ELECTRICITE("OuvrirFermerElectricite"),
  ENVOYER_TEAMS_MESSAGE_PREDEFINI("EnvoyerTeamsMessagePredefini"),
  OUVRIR_FERMER_PORTE("OuvrirFermerPorte"),
  VERROUILLER_DEVERROUILLER_PORTE("VerrouillerDeverrouillerPorte");

  private final String name;

  ActionName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
