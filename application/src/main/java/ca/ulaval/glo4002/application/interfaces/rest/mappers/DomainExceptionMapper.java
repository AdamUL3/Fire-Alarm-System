package ca.ulaval.glo4002.application.interfaces.rest.mappers;

import ca.ulaval.glo4002.application.domain.building.exception.BuildingNotFoundException;
import ca.ulaval.glo4002.application.domain.building.exception.RefusedAccessException;
import ca.ulaval.glo4002.application.domain.exception.DomainException;
import ca.ulaval.glo4002.application.domain.zone.exception.ZoneNotFoundException;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringNotFoundException;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionNotFoundException;
import ca.ulaval.glo4002.application.interfaces.rest.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

  private static final Logger LOGGER = Logger.getLogger(DomainExceptionMapper.class.getName());

  @Override
  public Response toResponse(DomainException e) {
    Response.StatusType statusCode = determineStatusCode(e);

    if (e.getMessage() != null) {
      ErrorResponseDTO errorResponse = ErrorResponseDTO.from(e.getMessage());
      return Response.status(statusCode).entity(errorResponse).build();
    }

    return Response.status(statusCode).type(MediaType.TEXT_PLAIN).entity("").build();
  }

  private Response.StatusType determineStatusCode(DomainException e) {
    if (e instanceof BuildingNotFoundException) {
      return Response.Status.NOT_FOUND;
    }

    if (e instanceof GatheringAlreadyExistsException
        || e instanceof GatheringNotFoundException
        || e instanceof InterventionAlreadyExistsException
        || e instanceof InterventionNotFoundException
        || e instanceof ZoneNotFoundException) {
      return Response.Status.BAD_REQUEST;
    }

    if (e instanceof RefusedAccessException) {
      return Response.Status.FORBIDDEN;
    }

    return Response.Status.INTERNAL_SERVER_ERROR;
  }
}
