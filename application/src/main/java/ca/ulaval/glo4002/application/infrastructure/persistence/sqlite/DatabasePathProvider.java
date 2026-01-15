package ca.ulaval.glo4002.application.infrastructure.persistence.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabasePathProvider {
  public static String getDatabasePath() {
    String dbPath = System.getProperty("dbPath");
    if (dbPath == null || dbPath.isBlank()) {
      String tmpDir = System.getProperty("java.io.tmpdir");
      Path path = Paths.get(tmpDir, "fire-system");

      try {
        Files.createDirectories(path);
      } catch (IOException e) {
        throw new RuntimeException("Fail to create directory: " + path, e);
      }

      dbPath = path.resolve("building_state.db").toString();
    }
    return dbPath;
  }
}
