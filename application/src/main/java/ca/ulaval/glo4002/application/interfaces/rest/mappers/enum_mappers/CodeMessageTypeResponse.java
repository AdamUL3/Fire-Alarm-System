package ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers;

import ca.ulaval.glo4002.application.domain.action.CodeMessageType;

public enum CodeMessageTypeResponse {
  FIRE_ALERT_IN_ROOM_YRE_RESP,
  STAY_WHERE_YOU_ARE,
  END_OF_ALERT;

  public static CodeMessageTypeResponse fromDomain(CodeMessageType codeMessageType) {
    return switch (codeMessageType) {
      case FIRE_ALERT_IN_ROOM -> FIRE_ALERT_IN_ROOM_YRE_RESP;
      case STAY -> STAY_WHERE_YOU_ARE;
      case END -> END_OF_ALERT;
    };
  }

  public String getValue() {
    return name();
  }
}
