package ca.ulaval.glo4002.application.domain.zone;

import ca.ulaval.glo4002.application.domain.ventilation.VentilationState;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.gathering.exception.GatheringNotFoundException;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionAlreadyExistsException;
import ca.ulaval.glo4002.application.domain.zone.intervention.exception.InterventionNotFoundException;
import java.util.List;

public class ZoneState {
  private FireState fireState;
  private boolean smokePresence;
  private VentilationState ventilationState;
  private final List<Gathering> gatherings;
  private final List<Intervention> interventions;

  public ZoneState(
      FireState fireState,
      boolean smokePresence,
      VentilationState ventilationState,
      List<Gathering> gatherings,
      List<Intervention> interventions) {
    this.fireState = fireState;
    this.smokePresence = smokePresence;
    this.ventilationState = ventilationState;
    this.gatherings = gatherings;
    this.interventions = interventions;
  }

  public FireState getFireState() {
    return fireState;
  }

  public void setFireState(FireState fireState) {
    this.fireState = fireState;
  }

  public boolean getSmokePresence() {
    return smokePresence;
  }

  public void setSmokePresence(boolean smokePresence) {
    this.smokePresence = smokePresence;
  }

  public int getNumberOfAgentsToSend(InterventionReason reason) {
    int totalAgents = 0;
    for (Intervention intervention : interventions) {
      if (intervention.getReason() == reason) {
        totalAgents += intervention.getAgentCount();
      }
    }
    totalAgents = 4 - totalAgents;
    if (totalAgents <= 0) {
      totalAgents = 0;
    }
    return totalAgents;
  }

  public int getNumberOfAgents() {
    int totalAgents = 0;
    for (Intervention intervention : interventions) {
      totalAgents += intervention.getAgentCount();
    }
    return totalAgents;
  }

  public VentilationState getVentilationState() {
    return ventilationState;
  }

  public void setVentilationState(VentilationState ventilationState) {
    this.ventilationState = ventilationState;
  }

  public boolean hasGathering(GatheringType gatheringType) {
    for (Gathering gathering : gatherings) {
      if (gathering.getType() == gatheringType) {
        return true;
      }
    }
    return false;
  }

  public void addGathering(Gathering gathering) {
    if (gatherings.contains(gathering)) {
      throw new GatheringAlreadyExistsException(gathering.getId());
    }
    gatherings.add(gathering);
  }

  public void removeGathering(GatheringID gatheringId) {
    for (Gathering gathering : gatherings) {
      if (gathering.getId().equals(gatheringId)) {
        gatherings.remove(gathering);
        return;
      }
    }
    throw new GatheringNotFoundException(gatheringId);
  }

  public List<Gathering> getGatherings() {
    return gatherings;
  }

  public boolean hasLargeGroup() {
    return getTotalExpectedPeopleCount() > 50;
  }

  public int getTotalExpectedPeopleCount() {
    int totalPeople = 0;
    for (Gathering gathering : gatherings) {
      totalPeople += gathering.getExpectedPeopleCount();
    }
    return totalPeople;
  }

  public void addIntervention(Intervention intervention) {
    if (interventions.contains(intervention)) {
      throw new InterventionAlreadyExistsException(intervention.getId());
    }

    interventions.add(intervention);
  }

  public void removeIntervention(InterventionID interventionId) {
    for (Intervention intervention : interventions) {
      if (intervention.getId().equals(interventionId)) {
        interventions.remove(intervention);
        return;
      }
    }
    throw new InterventionNotFoundException(interventionId);
  }

  public List<Intervention> getInterventions() {
    return interventions;
  }

  public InterventionReason getInterventionReason(InterventionID interventionId) {
    for (Intervention intervention : interventions) {
      if (intervention.getId().equals(interventionId)) {
        return intervention.getReason();
      }
    }
    throw new InterventionNotFoundException(interventionId);
  }
}
