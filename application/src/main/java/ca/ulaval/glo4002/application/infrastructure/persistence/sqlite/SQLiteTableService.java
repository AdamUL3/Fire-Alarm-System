package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import java.sql.*;

public class SQLiteTableService {

  public static void createTables() {
    String createBuildingsTable =
        """
                        CREATE TABLE IF NOT EXISTS buildings (
                            id TEXT PRIMARY KEY
                        );
                        """;

    String createZonesTable =
        """
                        CREATE TABLE IF NOT EXISTS zones (
                            id TEXT PRIMARY KEY,
                            building_id TEXT NOT NULL,
                            fire_state TEXT,
                            smoke_presence BOOLEAN,
                            ventilation_state TEXT,
                            ventilation_distribution_speed INTEGER,
                            ventilation_return_speed INTEGER,
                            FOREIGN KEY (building_id) REFERENCES buildings(id)
                        );
                        """;

    String createDoorsTable =
        """
                        CREATE TABLE IF NOT EXISTS doors (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            is_open BOOLEAN,
                            is_locked BOOLEAN,
                            FOREIGN KEY (zone_id) REFERENCES zones(id)
                        );
                        """;

    String createRoomsTable =
        """
                        CREATE TABLE IF NOT EXISTS rooms (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            is_electricity_open BOOLEAN,
                            room_occupancy INTEGER,
                            FOREIGN KEY (zone_id) REFERENCES zones(id)
                        );
                        """;

    String createRoomOccupancyTable =
        """
                    CREATE TABLE IF NOT EXISTS room_occupancy (
                    person_id TEXT PRIMARY KEY,
                    room_id TEXT NOT NULL
                    );
                    """;

    String createRoomsConsecutiveAccessesTable =
        """
                        CREATE TABLE IF NOT EXISTS rooms_consecutive_accesses (
                            room_id TEXT NOT NULL,
                            person_id TEXT NOT NULL,
                            accesses INTEGER,
                            PRIMARY KEY (room_id, person_id),
                            FOREIGN KEY (room_id) REFERENCES rooms(id)
                        );
                        """;

    String createGatheringsTable =
        """
                        CREATE TABLE IF NOT EXISTS gatherings (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            expected_person_count INTEGER,
                            type TEXT,
                            FOREIGN KEY (zone_id) REFERENCES zones(id)
                        );
                        """;

    String createInterventionsTable =
        """
                        CREATE TABLE IF NOT EXISTS interventions (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            priority TEXT,
                            reason TEXT,
                            number_of_agents INTEGER,
                            FOREIGN KEY (zone_id) REFERENCES zones(id)
                        );
                        """;

    // ---- Initial tables ----

    String createInitialBuildingsTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_buildings (
                            id TEXT PRIMARY KEY,
                            building_zone_id TEXT NOT NULL
                        );
                        """;

    String createInitialZonesTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_zones (
                            id TEXT PRIMARY KEY,
                            building_id TEXT NOT NULL,
                            fire_state TEXT,
                            smoke_presence BOOLEAN,
                            ventilation_state INTEGER,
                            ventilation_distribution_speed INTEGER,
                            ventilation_return_speed INTEGER,
                            building_zone_id TEXT NOT NULL,
                            FOREIGN KEY (building_id) REFERENCES initial_buildings(id)
                        );
                        """;

    String createInitialDoorsTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_doors (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            is_open BOOLEAN,
                            is_locked BOOLEAN,
                            building_zone_id TEXT NOT NULL,
                            FOREIGN KEY (zone_id) REFERENCES initial_zones(id)
                        );
                        """;

    String createInitialRoomsTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_rooms (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            is_electricity_open BOOLEAN,
                            building_zone_id TEXT NOT NULL,
                            FOREIGN KEY (zone_id) REFERENCES initial_zones(id)
                        );
                        """;

    String createInitialRoomsConsecutiveAccessesTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_rooms_consecutive_accesses (
                            room_id TEXT NOT NULL,
                            person_id TEXT NOT NULL,
                            accesses INTEGER,
                            building_zone_id TEXT NOT NULL,
                            PRIMARY KEY (room_id, person_id),
                            FOREIGN KEY (room_id) REFERENCES initial_rooms(id)
                        );
                        """;

    String createInitialGatheringsTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_gatherings (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            expected_person_count INTEGER,
                            type TEXT,
                            building_zone_id TEXT NOT NULL,
                            FOREIGN KEY (zone_id) REFERENCES initial_zones(id)
                        );
                        """;

    String createInitialInterventionsTable =
        """
                        CREATE TABLE IF NOT EXISTS initial_interventions (
                            id TEXT PRIMARY KEY,
                            zone_id TEXT NOT NULL,
                            priority TEXT,
                            reason TEXT,
                            number_of_agents INTEGER,
                            building_zone_id TEXT NOT NULL,
                            FOREIGN KEY (zone_id) REFERENCES initial_zones(id)
                        );
                        """;

    String createInitialRoomOccupancyTable =
        """
              CREATE TABLE IF NOT EXISTS initial_room_occupancy (
                  person_id TEXT NOT NULL,
                  room_id TEXT NOT NULL,
                  building_zone_id TEXT NOT NULL,
                  PRIMARY KEY (person_id, room_id),
                  FOREIGN KEY (room_id) REFERENCES initial_rooms(id)
              );
              """;

    String url = "jdbc:sqlite:" + DatabasePathProvider.getDatabasePath();
    try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {

      stmt.execute(createBuildingsTable);
      stmt.execute(createZonesTable);
      stmt.execute(createDoorsTable);
      stmt.execute(createRoomsTable);
      stmt.execute(createRoomOccupancyTable);
      stmt.execute(createRoomsConsecutiveAccessesTable);
      stmt.execute(createGatheringsTable);
      stmt.execute(createInterventionsTable);

      stmt.execute(createInitialBuildingsTable);
      stmt.execute(createInitialZonesTable);
      stmt.execute(createInitialDoorsTable);
      stmt.execute(createInitialRoomsTable);
      stmt.execute(createInitialRoomsConsecutiveAccessesTable);
      stmt.execute(createInitialRoomOccupancyTable);
      stmt.execute(createInitialGatheringsTable);
      stmt.execute(createInitialInterventionsTable);

    } catch (SQLException e) {
      throw new RuntimeException(e);
      //      throw new PersistenceException("Failed to create SQLite tables");
    }
  }
}
