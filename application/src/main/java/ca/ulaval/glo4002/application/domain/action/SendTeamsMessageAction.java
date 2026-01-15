package ca.ulaval.glo4002.application.domain.action;

import ca.ulaval.glo4002.application.domain.user.PersonID;

public class SendTeamsMessageAction extends Action {
  private final PersonID destinationId;
  private final CodeMessageType messageType;

  public SendTeamsMessageAction(PersonID destinationId, CodeMessageType messageType) {
    this.destinationId = destinationId;
    this.messageType = messageType;
  }

  public PersonID getDestinationId() {
    return destinationId;
  }

  public CodeMessageType getMessageType() {
    return messageType;
  }
}
