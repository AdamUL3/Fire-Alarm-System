package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import ca.ulaval.glo4002.application.domain.action.AgentPriority;
import ca.ulaval.glo4002.application.domain.action.OpenClosedState;
import ca.ulaval.glo4002.application.domain.building.BuildingID;
import ca.ulaval.glo4002.application.domain.door.DoorID;
import ca.ulaval.glo4002.application.domain.room.RoomID;
import ca.ulaval.glo4002.application.domain.user.PersonID;
import ca.ulaval.glo4002.application.domain.zone.FireState;
import ca.ulaval.glo4002.application.domain.zone.ZoneID;
import ca.ulaval.glo4002.application.domain.zone.gathering.Gathering;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringID;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringType;
import ca.ulaval.glo4002.application.domain.zone.intervention.Intervention;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionID;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionReason;
import ca.ulaval.glo4002.application.infrastructure.persistence.state.*;
import java.sql.*;
import java.util.*;

public class SQLiteQueryDAO {
  private final String url;

  public SQLiteQueryDAO() {
    this.url = "jdbc:sqlite:" + DatabasePathProvider.getDatabasePath();
  }

  public Connection connect() throws SQLException {
    return DriverManager.getConnection(url);
  }

  public Optional<BuildingStateEntity> findCurrentStateByBuildingId(String buildingId) {
    String query =
        """
                                    SELECT b.id as building_id,
                                           z.id as zone_id,
                                           z.fire_state,
                                           z.smoke_presence,
                                           z.ventilation_state,
                                           z.ventilation_distribution_speed,
                                           z.ventilation_return_speed,
                                           d.id as door_id,
                                           d.is_open,
                                           d.is_locked,
                                           r.id as room_id,
                                           r.is_electricity_open,
                                           r.room_occupancy,
                                           ro.person_id,
                                           ra.person_id as ra_person_id,
                                           ra.accesses as ra_accesses,
                                           g.id as gathering_id,
                                           g.expected_person_count,
                                           g.type as gathering_type,
                                           i.id as intervention_id,
                                           i.priority as intervention_priority,
                                           i.reason as intervention_reason,
                                           i.number_of_agents
                                    FROM buildings b
                                    LEFT JOIN zones z ON b.id = z.building_id
                                    LEFT JOIN doors d ON z.id = d.zone_id
                                    LEFT JOIN rooms r ON z.id = r.zone_id
                                        LEFT JOIN room_occupancy ro on r.id = ro.room_id
                                    LEFT JOIN rooms_consecutive_accesses ra ON r.id = ra.room_id
                                    LEFT JOIN gatherings g ON z.id = g.zone_id
                                    LEFT JOIN interventions i ON z.id = i.zone_id
                                    WHERE b.id = ?;
                                """;
    try (Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, buildingId);
      ResultSet rs = ps.executeQuery();
      return parseQueryResult(rs);
    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to load current state for building " + buildingId);
    }
  }

  public Optional<BuildingStateEntity> findInitialStateByBuildingAndZoneId(
      BuildingID buildingId, ZoneID zoneID) {
    String compositeId = buildingId.value() + "-" + zoneID.value();

    String query =
        """
                                          SELECT b.id as building_id,
                                                 z.id as zone_id,
                                                 z.fire_state,
                                                 z.smoke_presence,
                                                 z.ventilation_state,
                                                 z.ventilation_distribution_speed,
                                                 z.ventilation_return_speed,
                                                 d.id as door_id,
                                                 d.is_open,
                                                 d.is_locked,
                                                 r.id as room_id,
                                                 r.is_electricity_open,
                                                 ira.person_id as ra_person_id,
                                                 ira.accesses as ra_accesses,
                                                 iro.person_id as person_id,
                                                 g.id as gathering_id,
                                                 g.expected_person_count,
                                                 g.type as gathering_type,
                                                 i.id as intervention_id,
                                                 i.priority as intervention_priority,
                                                 i.reason as intervention_reason,
                                                 i.number_of_agents
                                          FROM initial_buildings b
                                          LEFT JOIN initial_zones z ON z.building_zone_id = ?
                                          LEFT JOIN initial_doors d ON d.building_zone_id = ?
                                          LEFT JOIN initial_rooms r ON r.building_zone_id = ?
                                          LEFT JOIN initial_rooms_consecutive_accesses ira ON r.id = ira.room_id AND ira.building_zone_id = ?
                                          LEFT JOIN initial_room_occupancy iro ON r.id = iro.room_id AND iro.building_zone_id = ?
                                          LEFT JOIN initial_gatherings g ON g.building_zone_id = ?
                                          LEFT JOIN initial_interventions i ON i.building_zone_id = ?
                                          WHERE b.building_zone_id = ?
                                      """;
    try (Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {
      for (int i = 1; i <= 8; i++) {
        ps.setString(i, compositeId);
      }
      ResultSet rs = ps.executeQuery();
      return parseQueryResult(rs);
    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to load initial state for building "
              + buildingId
              + " and zone "
              + zoneID);
    }
  }

  private Optional<BuildingStateEntity> parseQueryResult(ResultSet rs) throws SQLException {
    Map<String, ZoneStateEntity> zones = new LinkedHashMap<>();
    String buildingId = null;

    ResultSetMetaData meta = rs.getMetaData();
    int colCount = meta.getColumnCount();
    Set<String> cols = new HashSet<>(colCount * 2);
    for (int c = 1; c <= colCount; c++) {
      cols.add(meta.getColumnLabel(c).toLowerCase());
    }
    boolean hasRoomOccupancy = cols.contains("room_occupancy");
    boolean hasPersonId = cols.contains("person_id");

    while (rs.next()) {
      if (buildingId == null) {
        buildingId = rs.getString("building_id");
      }

      String zoneId = rs.getString("zone_id");
      String ventState = null;
      if (cols.contains("ventilation_state")) {
        ventState = rs.getString("ventilation_state");
      }

      if (zoneId != null) {
        zones.putIfAbsent(
            zoneId,
            new ZoneStateEntity(
                new ZoneID(zoneId),
                FireState.fromLabel(rs.getString("fire_state")),
                cols.contains("smoke_presence") ? rs.getBoolean("smoke_presence") : false,
                ventState != null ? OpenClosedState.valueOf(ventState) : null,
                cols.contains("ventilation_distribution_speed")
                    ? rs.getInt("ventilation_distribution_speed")
                    : 0,
                cols.contains("ventilation_return_speed")
                    ? rs.getInt("ventilation_return_speed")
                    : 0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));
      }

      String doorId = cols.contains("door_id") ? rs.getString("door_id") : null;
      if (doorId != null && zoneId != null) {
        ZoneStateEntity zone = zones.get(zoneId);
        if (zone != null) {
          zone.doors()
              .add(
                  new DoorStateEntity(
                      new DoorID(doorId),
                      cols.contains("is_open") ? rs.getBoolean("is_open") : false,
                      cols.contains("is_locked") ? rs.getBoolean("is_locked") : false));
        }
      }

      String roomId = cols.contains("room_id") ? rs.getString("room_id") : null;
      if (roomId != null && zoneId != null) {
        RoomStateEntity existingRoom = null;
        ZoneStateEntity zone = zones.get(zoneId);
        if (zone != null) {
          for (RoomStateEntity r : zone.rooms()) {
            if (r.id().value().equals(roomId)) {
              existingRoom = r;
              break;
            }
          }
        }

        String raPersonId = cols.contains("ra_person_id") ? rs.getString("ra_person_id") : null;
        Integer raAccesses = null;
        if (raPersonId != null && cols.contains("ra_accesses")) {
          int raw = rs.getInt("ra_accesses");
          if (!rs.wasNull()) {
            raAccesses = raw;
          } else {
            raAccesses = 0;
          }
        }

        if (existingRoom == null) {
          List<PersonID> personIDList = new ArrayList<>();
          Map<PersonID, Integer> consecutiveMap = new HashMap<>();
          if (raPersonId != null) {
            consecutiveMap.put(new PersonID(raPersonId), raAccesses != null ? raAccesses : 0);
          }

          int currentOccupancy = 0;
          if (hasRoomOccupancy) {
            int value = rs.getInt("room_occupancy");
            if (!rs.wasNull()) {
              currentOccupancy = value;
            }
          }

          RoomStateEntity roomEntity =
              new RoomStateEntity(
                  new RoomID(roomId),
                  cols.contains("is_electricity_open")
                      ? rs.getBoolean("is_electricity_open")
                      : false,
                  currentOccupancy,
                  personIDList,
                  consecutiveMap);

          if (zone != null) {
            zone.rooms().add(roomEntity);
          }
        } else {
          if (raPersonId != null) {
            Map<PersonID, Integer> map = existingRoom.consecutiveAccesses();
            if (map == null) {
              map = new HashMap<>();
            }
            map.put(new PersonID(raPersonId), raAccesses != null ? raAccesses : 0);
          }
        }
      }

      String gatheringId = cols.contains("gathering_id") ? rs.getString("gathering_id") : null;
      if (gatheringId != null && zoneId != null) {
        ZoneStateEntity zone = zones.get(zoneId);
        if (zone != null) {
          Gathering existingGathering = null;
          for (Gathering g : zone.gatherings()) {
            if (g.getId().value().equals(gatheringId)) {
              existingGathering = g;
              break;
            }
          }
          if (existingGathering == null) {
            int expected =
                cols.contains("expected_person_count") ? rs.getInt("expected_person_count") : 0;
            String typeLabel =
                cols.contains("gathering_type") ? rs.getString("gathering_type") : null;
            Gathering g =
                new Gathering(
                    new GatheringID(gatheringId),
                    expected,
                    typeLabel != null ? GatheringType.valueOf(typeLabel) : null);
            zone.gatherings().add(g);
          }
        }
      }

      String interventionId =
          cols.contains("intervention_id") ? rs.getString("intervention_id") : null;
      if (interventionId != null && zoneId != null) {
        ZoneStateEntity zone = zones.get(zoneId);
        if (zone != null) {
          Intervention existingIntervention = null;
          for (Intervention i : zone.interventions()) {
            if (i.getId().value().equals(interventionId)) {
              existingIntervention = i;
              break;
            }
          }
          if (existingIntervention == null) {
            String priorityLabel =
                cols.contains("intervention_priority")
                    ? rs.getString("intervention_priority")
                    : null;
            String reasonLabel =
                cols.contains("intervention_reason") ? rs.getString("intervention_reason") : null;

            InterventionReason reason = null;
            if (reasonLabel != null) {
              try {
                reason = InterventionReason.valueOf(reasonLabel);
              } catch (IllegalArgumentException e) {
                reason = null;
              }
            }

            int numberOfAgents =
                cols.contains("number_of_agents") ? rs.getInt("number_of_agents") : 0;

            Intervention intervention =
                new Intervention(
                    new InterventionID(interventionId),
                    priorityLabel != null ? AgentPriority.valueOf(priorityLabel) : null,
                    numberOfAgents,
                    reason);
            zone.interventions().add(intervention);
          }
        }
      }

      if (hasPersonId) {
        String personId = rs.getString("person_id");
        if (personId != null) {
          RoomStateEntity room = null;
          if (roomId != null) {
            room = findRoomInZones(zones, roomId);
          }
          if (room != null) {
            PersonID pid = new PersonID(personId);
            if (!room.identifiedOccupants().contains(pid)) {
              room.identifiedOccupants().add(pid);
            }
          }
        }
      }
    }

    if (buildingId == null) {
      return Optional.empty();
    }

    BuildingStateEntity building =
        new BuildingStateEntity(new BuildingID(buildingId), new ArrayList<>(zones.values()));
    return Optional.of(building);
  }

  private RoomStateEntity findRoomInZones(Map<String, ZoneStateEntity> zones, String roomId) {
    for (ZoneStateEntity z : zones.values()) {
      for (RoomStateEntity r : z.rooms()) {
        if (r.id().value().equals(roomId)) return r;
      }
    }
    return null;
  }

  public boolean saveCurrentState(BuildingStateEntity buildingStateEntity) {
    try (Connection conn = this.connect()) {
      String insertBuilding = "INSERT OR REPLACE INTO buildings (id) VALUES (?)";
      try (PreparedStatement buildingPs = conn.prepareStatement(insertBuilding)) {
        buildingPs.setString(1, buildingStateEntity.id().value());
        buildingPs.executeUpdate();
      }

      String insertZone =
          """
                                          INSERT OR REPLACE INTO zones
                                          (id, building_id, fire_state, smoke_presence, ventilation_state,  ventilation_distribution_speed, ventilation_return_speed)
                                          VALUES (?, ?, ?, ?, ?, ?, ?)
                                        """;

      String insertDoor =
          """
                                                INSERT OR REPLACE INTO doors (id, zone_id, is_open, is_locked)
                                                VALUES (?, ?, ?, ?)
                                              """;

      String insertRoom =
          """
                                                          INSERT OR REPLACE INTO rooms (id, zone_id, is_electricity_open, room_occupancy)
                                                          VALUES (?, ?, ?, ?)
                                                          """;

      String insertRoomOccupancy =
          """
              INSERT OR REPLACE INTO room_occupancy (person_id, room_id)
              VALUES (?, ?)
              """;

      String insertRoomAccess =
          "INSERT OR REPLACE INTO rooms_consecutive_accesses (room_id, person_id, accesses) VALUES (?, ?, ?)";

      String insertGathering =
          "INSERT OR REPLACE INTO gatherings (id, zone_id, expected_person_count, type) VALUES (?, ?, ?, ?)";

      String insertIntervention =
          "INSERT OR REPLACE INTO interventions (id, zone_id, priority, reason, number_of_agents) VALUES (?, ?, ?, ?, ?)";

      try (PreparedStatement zonePs = conn.prepareStatement(insertZone);
          PreparedStatement doorPs = conn.prepareStatement(insertDoor);
          PreparedStatement roomPs = conn.prepareStatement(insertRoom);
          PreparedStatement roomOccupancyPs = conn.prepareStatement(insertRoomOccupancy);
          PreparedStatement roomAccessPs = conn.prepareStatement(insertRoomAccess);
          PreparedStatement gatheringPs = conn.prepareStatement(insertGathering);
          PreparedStatement interventionPs = conn.prepareStatement(insertIntervention)) {

        for (ZoneStateEntity zone : buildingStateEntity.zones()) {
          try {
            for (DoorStateEntity door : zone.doors()) {
              doorPs.setString(1, door.id().value());
              doorPs.setString(2, zone.id().value());
              doorPs.setBoolean(3, door.openClosedState());
              doorPs.setBoolean(4, door.lockUnlockState());
              doorPs.addBatch();
            }

            for (RoomStateEntity room : zone.rooms()) {
              roomPs.setString(1, room.id().value());
              roomPs.setString(2, zone.id().value());
              roomPs.setBoolean(3, room.is_electricity_open());
              roomPs.setInt(4, room.currentOccupancy());
              roomPs.addBatch();

              if (room.identifiedOccupants() != null) {
                for (PersonID personID : room.identifiedOccupants()) {
                  roomOccupancyPs.setString(1, personID.value());
                  roomOccupancyPs.setString(2, room.id().value());
                  roomOccupancyPs.addBatch();
                }
              }
              removeConsecutiveAccesses(room.id());
              if (room.consecutiveAccesses() != null) {
                for (Map.Entry<PersonID, Integer> entry : room.consecutiveAccesses().entrySet()) {
                  roomAccessPs.setString(1, room.id().value());
                  roomAccessPs.setString(2, entry.getKey().value());
                  roomAccessPs.setInt(3, entry.getValue());
                  roomAccessPs.addBatch();
                }
              }
            }

            removeGatheringsAndInterventions(zone.id());

            for (Gathering g : zone.gatherings()) {
              gatheringPs.setString(1, g.getId().value());
              gatheringPs.setString(2, zone.id().value());
              gatheringPs.setInt(3, g.getExpectedPeopleCount());
              gatheringPs.setString(4, g.getType().name());
              gatheringPs.addBatch();
            }

            for (Intervention i : zone.interventions()) {
              interventionPs.setString(1, i.getId().value());
              interventionPs.setString(2, zone.id().value());
              interventionPs.setString(3, i.getPriority().name());
              interventionPs.setString(4, i.getReason() != null ? i.getReason().name() : null);
              interventionPs.setInt(5, i.getAgentCount());
              interventionPs.addBatch();
            }

            zonePs.setString(1, zone.id().value());
            zonePs.setString(2, buildingStateEntity.id().value());
            zonePs.setString(3, zone.fireState().toString());
            zonePs.setBoolean(4, zone.smokePresence());
            zonePs.setObject(
                5, zone.ventilationState() != null ? zone.ventilationState().isOpen() : null);
            zonePs.setObject(6, zone.ventDistributionSpeed());
            zonePs.setObject(7, zone.ventReturnSpeed());

            zonePs.addBatch();
          } catch (SQLException e) {
            throw new PersistenceException(
                "SQLiteQueryService: Failed to save zone " + zone.id().value());
          }
        }

        zonePs.executeBatch();
        doorPs.executeBatch();
        roomPs.executeBatch();
        roomOccupancyPs.executeBatch();
        roomAccessPs.executeBatch();
        gatheringPs.executeBatch();
        interventionPs.executeBatch();
      }

    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to save building state " + buildingStateEntity.id());
    }
    return true;
  }

  public boolean saveInitialState(BuildingStateEntity buildingStateEntity, ZoneID zoneID) {
    String compositeId = buildingStateEntity.id().value() + "-" + zoneID.value();

    try (Connection conn = this.connect()) {
      String insertBuilding =
          "INSERT OR REPLACE INTO initial_buildings (id, building_zone_id) VALUES (?, ?)";
      try (PreparedStatement buildingPs = conn.prepareStatement(insertBuilding)) {
        buildingPs.setString(1, buildingStateEntity.id().value());
        buildingPs.setString(2, compositeId);
        buildingPs.executeUpdate();
      }

      String insertZone =
          """
                                                  INSERT OR REPLACE INTO initial_zones
                                                  (id, building_id, fire_state, smoke_presence, ventilation_state,  ventilation_distribution_speed, ventilation_return_speed, building_zone_id)
                                                  VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                                                """;

      String insertDoor =
          """
                                                  INSERT OR REPLACE INTO initial_doors (id, zone_id, is_open, is_locked, building_zone_id)
                                                  VALUES (?, ?, ?, ?, ?)
                                                """;

      String insertRoom =
          """
                                                      INSERT OR REPLACE INTO initial_rooms (id, zone_id, is_electricity_open, building_zone_id)
                                                      VALUES (?, ?, ?, ?)
                                                      """;

      String insertInitialRoomAccess =
          "INSERT OR REPLACE INTO initial_rooms_consecutive_accesses (room_id, person_id, accesses, building_zone_id) VALUES (?, ?, ?, ?)";

      String insertInitialRoomOccupancy =
          "INSERT OR REPLACE INTO initial_room_occupancy (person_id, room_id, building_zone_id) VALUES (?, ?, ?)";

      String insertInitialGathering =
          "INSERT OR REPLACE INTO initial_gatherings (id, zone_id, expected_person_count, type, building_zone_id) VALUES (?, ?, ?, ?, ?)";

      String insertInitialIntervention =
          "INSERT OR REPLACE INTO initial_interventions (id, zone_id, priority, reason, number_of_agents, building_zone_id) VALUES (?, ?, ?, ?, ?, ?)";

      try (PreparedStatement zonePs = conn.prepareStatement(insertZone);
          PreparedStatement doorPs = conn.prepareStatement(insertDoor);
          PreparedStatement roomPs = conn.prepareStatement(insertRoom);
          PreparedStatement initialRoomAccessPs = conn.prepareStatement(insertInitialRoomAccess);
          PreparedStatement initialRoomOccupancyPs =
              conn.prepareStatement(insertInitialRoomOccupancy);
          PreparedStatement gatheringPs = conn.prepareStatement(insertInitialGathering);
          PreparedStatement interventionPs = conn.prepareStatement(insertInitialIntervention)) {

        for (ZoneStateEntity zone : buildingStateEntity.zones()) {
          try {
            for (DoorStateEntity door : zone.doors()) {
              doorPs.setString(1, door.id().value());
              doorPs.setString(2, zone.id().value());
              doorPs.setBoolean(3, door.openClosedState());
              doorPs.setBoolean(4, door.lockUnlockState());
              doorPs.setString(5, compositeId);
              doorPs.addBatch();
            }

            for (RoomStateEntity room : zone.rooms()) {
              roomPs.setString(1, room.id().value());
              roomPs.setString(2, zone.id().value());
              roomPs.setBoolean(3, room.is_electricity_open());
              roomPs.setString(4, compositeId);
              roomPs.addBatch();

              if (room.consecutiveAccesses() != null) {
                for (Map.Entry<PersonID, Integer> entry : room.consecutiveAccesses().entrySet()) {
                  initialRoomAccessPs.setString(1, room.id().value());
                  initialRoomAccessPs.setString(2, entry.getKey().value());
                  initialRoomAccessPs.setInt(3, entry.getValue());
                  initialRoomAccessPs.setString(4, compositeId);
                  initialRoomAccessPs.addBatch();
                }
              }

              if (room.identifiedOccupants() != null) {
                for (PersonID occupant : room.identifiedOccupants()) {
                  initialRoomOccupancyPs.setString(1, occupant.value());
                  initialRoomOccupancyPs.setString(2, room.id().value());
                  initialRoomOccupancyPs.setString(3, compositeId);
                  initialRoomOccupancyPs.addBatch();
                }
              }
            }

            for (Gathering g : zone.gatherings()) {
              gatheringPs.setString(1, g.getId().value());
              gatheringPs.setString(2, zone.id().value());
              gatheringPs.setInt(3, g.getExpectedPeopleCount());
              gatheringPs.setString(4, g.getType().name());
              gatheringPs.setString(5, compositeId);
              gatheringPs.addBatch();
            }

            for (Intervention i : zone.interventions()) {
              interventionPs.setString(1, i.getId().value());
              interventionPs.setString(2, zone.id().value());
              interventionPs.setString(3, i.getPriority().name());
              interventionPs.setString(4, i.getReason() != null ? i.getReason().name() : null);
              interventionPs.setInt(5, i.getAgentCount());
              interventionPs.setString(6, compositeId);
              interventionPs.addBatch();
            }

            zonePs.setString(1, zone.id().value());
            zonePs.setString(2, buildingStateEntity.id().value());
            zonePs.setString(3, zone.fireState().toString());
            zonePs.setBoolean(4, zone.smokePresence());
            zonePs.setObject(
                5, zone.ventilationState() != null ? zone.ventilationState().isOpen() : null);
            zonePs.setInt(6, zone.ventDistributionSpeed());
            zonePs.setInt(7, zone.ventReturnSpeed());
            zonePs.setString(8, compositeId);
            zonePs.addBatch();

          } catch (SQLException e) {
            throw new PersistenceException(
                "SQLiteQueryService: Failed to prepare zone " + zone.id().value());
          }
        }

        zonePs.executeBatch();
        doorPs.executeBatch();
        roomPs.executeBatch();
        initialRoomAccessPs.executeBatch();
        initialRoomOccupancyPs.executeBatch();
        gatheringPs.executeBatch();
        interventionPs.executeBatch();

        System.out.println("Saved initial state with composite ID: " + compositeId);
      }

    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to save building state " + buildingStateEntity.id());
    }
    return true;
  }

  public boolean deleteInitialState(BuildingID buildingID, ZoneID zoneID) {
    String compositeId = buildingID.value() + "-" + zoneID.value();

    String buildingDelete =
        String.format("DELETE FROM initial_buildings WHERE building_zone_id = '%s'", compositeId);

    String zoneDelete =
        String.format("DELETE FROM initial_zones WHERE building_zone_id = '%s'", compositeId);

    String doorDelete =
        String.format("DELETE FROM initial_doors WHERE building_zone_id = '%s'", compositeId);

    String roomDelete =
        String.format("DELETE FROM initial_rooms WHERE building_zone_id = '%s'", compositeId);

    String roomAccessDelete =
        String.format(
            "DELETE FROM initial_rooms_consecutive_accesses WHERE building_zone_id = '%s'",
            compositeId);

    String roomOccupancyDelete =
        String.format(
            "DELETE FROM initial_room_occupancy WHERE building_zone_id = '%s'", compositeId);

    String gatheringDelete =
        String.format("DELETE FROM initial_gatherings WHERE building_zone_id = '%s'", compositeId);

    String interventionDelete =
        String.format(
            "DELETE FROM initial_interventions WHERE building_zone_id = '%s'", compositeId);

    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(buildingDelete);
      stmt.executeUpdate(zoneDelete);
      stmt.executeUpdate(doorDelete);
      stmt.executeUpdate(roomDelete);
      stmt.executeUpdate(roomAccessDelete);
      stmt.executeUpdate(roomOccupancyDelete);
      stmt.executeUpdate(gatheringDelete);
      stmt.executeUpdate(interventionDelete);
      return true;
    } catch (SQLException e) {
      throw new PersistenceException("Failed deleting initial state for " + compositeId);
    }
  }

  public void resetDatabase() {
    String query =
        """
                                       DELETE FROM buildings;
                                       DELETE FROM zones;
                                       DELETE FROM gatherings;
                                       DELETE FROM interventions;
                                       DELETE FROM doors;
                                       DELETE FROM rooms;
                                       DELETE FROM rooms_consecutive_accesses;
                                       DELETE FROM initial_buildings;
                                       DELETE FROM initial_zones;
                                       DELETE FROM initial_gatherings;
                                       DELETE FROM initial_interventions;
                                       DELETE FROM initial_doors;
                                       DELETE FROM initial_rooms;
                                       DELETE FROM initial_rooms_consecutive_accesses;
                                       DELETE FROM initial_room_occupancy;
                                      """;

    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      throw new PersistenceException("Failed to reset database");
    }
  }

  private void removeGatheringsAndInterventions(ZoneID zoneID) {
    String gatheringsQuery =
        String.format("DELETE FROM gatherings WHERE zone_id = '%s'", zoneID.value());
    String interventionsQuery =
        String.format("DELETE FROM interventions WHERE zone_id = '%s'", zoneID.value());
    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(gatheringsQuery);
      stmt.executeUpdate(interventionsQuery);
    } catch (SQLException e) {
      throw new PersistenceException(
          "Failed to reset gatherings and interventions for zone " + zoneID.value());
    }
  }

  private void removeConsecutiveAccesses(RoomID roomID) {
    String query =
        String.format(
            "DELETE FROM rooms_consecutive_accesses WHERE room_id = '%s'", roomID.value());
    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      throw new PersistenceException(
          "Failed to reset room_consecutive_accesses for room " + roomID.value());
    }
  }
}
