package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.enum_mappers.*;
import java.util.ArrayList;
import java.util.List;

public class ActionMapper {
  private final ParamMapper paramMapper;

  public ActionMapper(ParamMapper paramMapper) {
    this.paramMapper = paramMapper;
  }

  public ActionDTO toDTO(Action action) {
    return switch (action) {
      case ActivateFireAlarmAction a -> toActivateFireAlarmDTO(a);
      case CallFirefightersAction a -> toCallFirefightersDTO(a);
      case RequestAgentAction a -> toRequestAgentDTO(a);
      case SendSMSMessageAction a -> toSendSMSMessageDTO(a);
      case SendTeamsMessageAction a -> toSendTeamsMessageDTO(a);
      case OpenClosePowerAction a -> toOpenClosePowerDTO(a);
      case OpenCloseVentilationAction a -> toOpenCloseVentilationDTO(a);
      case AdjustVentilationSpeedAction a -> toAdjustVentilationSpeedDTO(a);
      case LockUnlockDoorAction a -> toLockUnlockDoorDTO(a);
      case OpenCloseDoorAction a -> toOpenCloseDoorDTO(a);
      case null, default -> null;
    };
  }

  private ActionDTO toActivateFireAlarmDTO(ActivateFireAlarmAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toRingState(AlarmStateResponse.fromDomain(action.getAlarmState())));
    return new ActionDTO(ActionName.ACTIVER_ALARME_INCENDIE.getName(), params);
  }

  private ActionDTO toCallFirefightersDTO(CallFirefightersAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuildingAddress(action.getBuildingAddress().value()));
    params.add(paramMapper.toReason(CallReasonTypeResponse.fromDomain(action.getCallReason())));
    return new ActionDTO(ActionName.APPELER_POMPIERS.getName(), params);
  }

  private ActionDTO toRequestAgentDTO(RequestAgentAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toAgentType(AgentTypeResponse.fromDomain(action.getAgentType())));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toPriority(AgentPriorityResponse.fromDomain(action.getPriority())));
    params.add(paramMapper.toInterventionId(action.getInterventionId().value()));
    if (action.getAgentId() != null) {
      params.add(paramMapper.toAgentId(action.getAgentId()));
    }
    return new ActionDTO(ActionName.DEMANDER_AGENT.getName(), params);
  }

  private ActionDTO toSendSMSMessageDTO(SendSMSMessageAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toPhoneNumber(action.getPhoneNumber().getValue()));
    params.add(paramMapper.toSupervisorName(action.getSupervisorName().value()));
    params.add(
        paramMapper.toMessageType(CodeMessageTypeResponse.fromDomain(action.getMessageType())));
    return new ActionDTO(ActionName.ENVOYER_SMS_MESSAGE_PREDEFINI.getName(), params);
  }

  private ActionDTO toSendTeamsMessageDTO(SendTeamsMessageAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toDestinationId(action.getDestinationId().value()));
    params.add(
        paramMapper.toMessageType(CodeMessageTypeResponse.fromDomain(action.getMessageType())));
    return new ActionDTO(ActionName.ENVOYER_TEAMS_MESSAGE_PREDEFINI.getName(), params);
  }

  private ActionDTO toOpenClosePowerDTO(OpenClosePowerAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toRoom(action.getRoomId().value()));
    params.add(
        paramMapper.toOpenClosedState(
            OpenClosedStateResponse.fromDomain(action.getElectricityState())));
    return new ActionDTO(ActionName.OUVRIR_FERMER_ELECTRICITE.getName(), params);
  }

  private ActionDTO toOpenCloseVentilationDTO(OpenCloseVentilationAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(
        paramMapper.toOpenClosedState(
            OpenClosedStateResponse.fromDomain(action.getVentilationState())));
    return new ActionDTO(ActionName.OUVRIR_FERMER_VENTILATION.getName(), params);
  }

  private ActionDTO toAdjustVentilationSpeedDTO(AdjustVentilationSpeedAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toDistribution(action.getDistributionSpeed()));
    params.add(paramMapper.toReturnQuantity(action.getReturnQuantity()));
    return new ActionDTO(ActionName.REGLER_VITESSE_VENTILATION.getName(), params);
  }

  private ActionDTO toLockUnlockDoorDTO(LockUnlockDoorAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toDoor(action.getDoorId().value()));
    params.add(
        paramMapper.toLockUnlockedState(
            LockedUnlockedStateResponse.fromDomain(action.getLockState())));
    return new ActionDTO(ActionName.VERROUILLER_DEVERROUILLER_PORTE.getName(), params);
  }

  private ActionDTO toOpenCloseDoorDTO(OpenCloseDoorAction action) {
    List<ParamDTO> params = new ArrayList<>();
    params.add(paramMapper.toBuilding(action.getBuildingId().value()));
    params.add(paramMapper.toZone(action.getZoneId().value()));
    params.add(paramMapper.toDoor(action.getDoorId().value()));
    params.add(
        paramMapper.toOpenClosedState(OpenClosedStateResponse.fromDomain(action.getOpenState())));
    return new ActionDTO(ActionName.OUVRIR_FERMER_PORTE.getName(), params);
  }
}
