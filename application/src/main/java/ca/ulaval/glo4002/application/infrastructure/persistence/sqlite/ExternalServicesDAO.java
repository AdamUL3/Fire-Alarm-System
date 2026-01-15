package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import ca.ulaval.glo4002.application.domain.access.AccessRole;
import ca.ulaval.glo4002.application.domain.access.CardID;
import ca.ulaval.glo4002.application.domain.user.*;
import java.sql.*;

public class ExternalServicesDAO {
  private final String url;

  public ExternalServicesDAO() {
    this.url = "jdbc:sqlite:" + DatabasePathProvider.getDatabasePath();
  }

  public Connection connect() throws SQLException {
    return DriverManager.getConnection(url);
  }

  public PersonInformation findByPersonId(PersonID id) {
    String query =
        String.format("select idul, nom, no_contact, etat from users where idul='%s'", id.value());

    try (Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ResultSet rs = ps.executeQuery();
      PersonName personName = new PersonName(rs.getString("nom"));
      PhoneNumber phoneNumber = new PhoneNumber(rs.getString("no_contact"));
      PersonID personID = new PersonID(rs.getString("idul"));

      ResponsibleStatus responsibleStatus = ResponsibleStatus.AVAILABLE;
      String status = rs.getString("etat");
      switch (status) {
        case "IN_A_MEETING":
          responsibleStatus = ResponsibleStatus.IN_A_MEETING;
        case "OUT_OF_OFFICE":
          responsibleStatus = ResponsibleStatus.OUT_OF_OFFICE;
      }

      return new PersonInformation(personName, phoneNumber, personID, responsibleStatus);
    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to load information for " + id.value());
    }
  }

  public PersonAccessInformation findByCardNumber(CardID cardId) {
    String query =
        """
        select id_carte, idul, role_securite
        from cartes_acces
        where id_carte=?
        """;

    try (Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {

      ps.setInt(1, Integer.parseInt(cardId.value()));
      ResultSet rs = ps.executeQuery();

      if (!rs.next()) {
        return null;
      }

      CardID cardID = new CardID(String.valueOf(rs.getInt("id_carte")));
      PersonID personId = new PersonID(rs.getString("idul"));

      AccessRole role =
          switch (rs.getString("role_securite")) {
            case "AGENT-SECURITE" -> AccessRole.SECURITY_AGENT;
            case null, default -> AccessRole.REGULAR;
          };

      return new PersonAccessInformation(cardID, personId, role, false);

    } catch (SQLException e) {
      throw new PersistenceException(
          "SQLiteQueryService: Failed to load information for card " + cardId.value());
    }
  }
}
