package ca.ulaval.glo4002.application.interfaces.rest;

import ca.ulaval.glo4002.application.application.EventService;
import ca.ulaval.glo4002.application.domain.action.Action;
import ca.ulaval.glo4002.application.infrastructure.ApplicationFactory;
import ca.ulaval.glo4002.application.interfaces.rest.dto.SecurityEventDTO;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.ActionMapper;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.ParamMapper;
import ca.ulaval.glo4002.application.interfaces.rest.mappers.SecurityEventMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/evenement-securite")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {
  private final EventService eventService = ApplicationFactory.createEventService();
  private final SecurityEventMapper eventMapper = new SecurityEventMapper();
  private final ParamMapper paramMapper = new ParamMapper();
  private final ActionMapper actionMapper = new ActionMapper(paramMapper);

  @POST
  public EventResponse event(SecurityEventDTO eventSecuriteDTO) {
    List<Action> actions = eventService.handleEvent(eventMapper.toEvent(eventSecuriteDTO));
    return new EventResponse(actions.stream().map(actionMapper::toDTO).toList());
  }
}
