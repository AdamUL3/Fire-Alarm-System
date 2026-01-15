package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.action.AgentID;
import ca.ulaval.glo4002.application.domain.action.ParamDTO;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers.*;

public class ParamMapper {

  public ParamDTO toBuilding(String buildingId) {
    return new ParamDTO("batiment", buildingId);
  }

  public ParamDTO toZone(String zoneId) {
    return new ParamDTO("zone", zoneId);
  }

  public ParamDTO toRoom(String roomId) {
    return new ParamDTO("local", roomId);
  }

  public ParamDTO toDoor(String doorId) {
    return new ParamDTO("porte", doorId);
  }

  public ParamDTO toRingState(AlarmStateResponse alarmState) {
    return new ParamDTO("sonnerie", alarmState.getValue());
  }

  public ParamDTO toBuildingAddress(String address) {
    return new ParamDTO("adresseBatiment", address);
  }

  public ParamDTO toReason(CallReasonTypeResponse reason) {
    return new ParamDTO("raison", reason.getValue());
  }

  public ParamDTO toAgentType(AgentTypeResponse agentType) {
    return new ParamDTO("typeAgent", agentType.getValue());
  }

  public ParamDTO toPriority(AgentPriorityResponse agentPriority) {
    return new ParamDTO("priorite", agentPriority.getValue());
  }

  public ParamDTO toInterventionId(String id) {
    return new ParamDTO("idIntervention", id);
  }

  public ParamDTO toAgentId(AgentID id) {
    return new ParamDTO("agentId", id.value());
  }

  public ParamDTO toPhoneNumber(String number) {
    return new ParamDTO("numeroDestination", number);
  }

  public ParamDTO toSupervisorName(String name) {
    return new ParamDTO("nomDestination", name);
  }

  public ParamDTO toMessageType(CodeMessageTypeResponse messageType) {
    return new ParamDTO("codeMessage", messageType.getValue());
  }

  public ParamDTO toDestinationId(String id) {
    return new ParamDTO("idDestination", id);
  }

  public ParamDTO toDistribution(int value) {
    return new ParamDTO("distribution", String.valueOf(value));
  }

  public ParamDTO toReturnQuantity(int value) {
    return new ParamDTO("retour", String.valueOf(value));
  }

  public ParamDTO toLockUnlockedState(LockedUnlockedStateResponse lockedUnlockedState) {
    return new ParamDTO("valeur", lockedUnlockedState.getValue());
  }

  public ParamDTO toOpenClosedState(OpenClosedStateResponse openState) {
    return new ParamDTO("valeur", openState.getValue());
  }
}
