package ca.ulaval.glo4002.application.domain.room;

import ca.ulaval.glo4002.application.domain.user.PersonID;
import java.util.List;
import java.util.Map;

public class RoomState {

  private final boolean isElectricityOpen;
  private int currentOccupancy;
  private final List<PersonID> identifiedOccupants;
  private final Map<PersonID, Integer> consecutiveAccesses;

  public RoomState(
      boolean isElectricityOpen,
      int currentOccupancy,
      List<PersonID> identifiedOccupants,
      Map<PersonID, Integer> consecutiveAccesses) {
    this.isElectricityOpen = isElectricityOpen;
    this.currentOccupancy = currentOccupancy;
    this.identifiedOccupants = identifiedOccupants;
    this.consecutiveAccesses = consecutiveAccesses;
  }

  public boolean isElectricityOpen() {
    return isElectricityOpen;
  }

  public int getCurrentOccupancy() {
    return currentOccupancy;
  }

  public List<PersonID> getIdentifiedOccupants() {
    return identifiedOccupants;
  }

  public void addOccupant(PersonID personId) {
    if (personId != null) {
      if (!identifiedOccupants.contains(personId)) {
        identifiedOccupants.add(personId);
        this.currentOccupancy += 1;
      }
    } else {
      this.currentOccupancy += 1;
    }
  }

  public void removeOccupant() {
    if (this.currentOccupancy > 0) {
      this.currentOccupancy -= 1;
    }
  }

  public boolean registerConsecutiveAccess(PersonID personId) {
    consecutiveAccesses.merge(personId, 1, Integer::sum);
    if (consecutiveAccesses.get(personId) >= ConsecutiveAccessThreshold.THRESHOLD.getValue()) {
      consecutiveAccesses.clear();
      return true;
    }
    return false;
  }

  public Map<PersonID, Integer> getConsecutiveAccesses() {
    return consecutiveAccesses;
  }
}
