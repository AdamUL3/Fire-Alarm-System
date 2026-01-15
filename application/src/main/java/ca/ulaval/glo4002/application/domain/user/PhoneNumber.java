package ca.ulaval.glo4002.application.domain.user;

public class PhoneNumber {
  private final String value;

  public PhoneNumber(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
