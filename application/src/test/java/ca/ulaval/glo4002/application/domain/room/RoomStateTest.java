package ca.ulaval.glo4002.application.domain.room;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4002.application.domain.user.PersonID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomStateTest {

  private RoomState roomState;
  private List<PersonID> occupants;
  private HashMap<PersonID, Integer> consecutiveAccesses;

  @BeforeEach
  void setUp() {
    occupants = new ArrayList<>();
    roomState = new RoomState(true, 0, occupants, consecutiveAccesses);
  }

  @Test
  void givenUserID_whenAddOccupant_thenOccupantIsAddedToList() {
    PersonID user = new PersonID("user1");

    roomState.addOccupant(user);

    assertTrue(occupants.contains(user));
  }

  @Test
  void givenNullUserID_whenAddOccupant_thenListDoesNotChange() {
    roomState.addOccupant(null);

    assertTrue(occupants.isEmpty());
  }

  @Test
  void givenUserAdded_whenAddOccupant_thenCurrentOccupancyIncreases() {
    roomState.addOccupant(new PersonID("user1"));

    assertEquals(1, roomState.getCurrentOccupancy());
  }

  @Test
  void givenMultipleUsersAdded_whenAddOccupant_thenCurrentOccupancyAccumulates() {
    roomState.addOccupant(new PersonID("user1"));
    roomState.addOccupant(new PersonID("user2"));

    assertEquals(2, roomState.getCurrentOccupancy());
  }

  @Test
  void givenCurrentOccupancyPositive_whenRemoveOccupant_thenCurrentOccupancyDecreases() {
    roomState.addOccupant(new PersonID("user1"));

    roomState.removeOccupant();

    assertEquals(0, roomState.getCurrentOccupancy());
  }

  @Test
  void givenCurrentOccupancyZero_whenRemoveOccupant_thenCurrentOccupancyStaysZero() {
    roomState.removeOccupant();

    assertEquals(0, roomState.getCurrentOccupancy());
  }
}
