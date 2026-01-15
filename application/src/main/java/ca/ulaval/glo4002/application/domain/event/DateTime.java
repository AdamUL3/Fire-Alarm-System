package ca.ulaval.glo4002.application.domain.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DateTime(LocalDateTime value) {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public DateTime(String value) {
    this(LocalDateTime.parse(value, FORMATTER));
  }

  public String toIsoString() {
    return value.format(FORMATTER);
  }

  @Override
  public String toString() {
    return toIsoString();
  }
}
