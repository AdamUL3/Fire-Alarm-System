package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ExternalServicesTableService {
  public static void createTables() {
    String createUsersTable =
        """
                        CREATE TABLE IF NOT EXISTS users (
                        idul TEXT PRIMARY KEY,
                        nom TEXT,
                        no_contact TEXT NOT NULL,
                        etat TEXT
                        )
                        """;

    String populateUsersTable =
        """
                        INSERT OR IGNORE INTO users (idul, nom, no_contact, etat) VALUES
                        ('jdoe', 'John Doe', '+15145551234', 'AVAILABLE'),
                        ('asmith', 'Alice Smith', '+15145554321', 'IN_A_MEETING'),
                        ('bjones', 'Bob Jones', '+15145559876', 'OUT_OF_OFFICE')
                        """;

    String createAccessCardsTable =
        """
                        CREATE TABLE IF NOT EXISTS cartes_acces (
                            id_carte INTEGER PRIMARY KEY,
                            idul TEXT NOT NULL,
                            role_securite TEXT
                        )
                        """;

    String populateAccessCardsTable =
        """
                        INSERT OR IGNORE INTO  cartes_acces (id_carte, idul, role_securite) VALUES
                        (123456, 'jdoe', NULL),
                        (789012, 'asmith', NULL),
                        (345678, 'bjones', NULL),
                        (384643, 'dgijr', 'AGENT-SECURITE'),
                        (746291, 'emart', 'AGENT-SECURITE')
                        """;

    String url = "jdbc:sqlite:" + DatabasePathProvider.getDatabasePath();
    try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {

      stmt.execute(createUsersTable);
      stmt.execute(populateUsersTable);

      stmt.execute(createAccessCardsTable);
      stmt.execute(populateAccessCardsTable);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
