package ca.ulaval.glo4002.application.interfaces.rest.dto;

public class AccessRequestDTO {
  public String card_id;
  public String zone_id;
  public String room_id;
  public String door_id;
  public String date_heure;

  public AccessRequestDTO() {}
  ;

  AccessRequestDTO(
      String card_id, String zone_id, String room_id, String door_id, String date_heure) {
    this.card_id = card_id;
    this.zone_id = zone_id;
    this.room_id = room_id;
    this.door_id = door_id;
    this.date_heure = date_heure;
  }

  public String getCardId() {
    return card_id;
  }

  public String getZoneId() {
    return zone_id;
  }

  public String getRoomId() {
    return room_id;
  }

  public String getDoorId() {
    return door_id;
  }

  public String getDateTime() {
    return date_heure;
  }
}
