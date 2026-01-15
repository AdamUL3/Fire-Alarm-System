package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.user.PersonName;
import ca.ulaval.glo4002.application.domain.user.PhoneNumber;

public class SendSMSMessageAction extends Action {
  private final PhoneNumber phoneNumber;
  private final PersonName supervisorName;
  private final CodeMessageType messageType;

  public SendSMSMessageAction(
      PhoneNumber phoneNumber, PersonName supervisorName, CodeMessageType messageType) {
    this.phoneNumber = phoneNumber;
    this.supervisorName = supervisorName;
    this.messageType = messageType;
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public PersonName getSupervisorName() {
    return supervisorName;
  }

  public CodeMessageType getMessageType() {
    return messageType;
  }

  public PhoneNumber getDestinationNumber() {
    return phoneNumber;
  }
}
