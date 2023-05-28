package Shared;

import Server.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DownloadHandler {
    Database database;

    public DownloadHandler(Database database) {
        this.database = database;
    }

    public void Download(int gameId, int userId) throws SQLException {
        String query = "INSERT INTO downloads (account_id, game_id) VALUES (?,?)";
        PreparedStatement stmt = database.getConnection().prepareStatement(query);

        stmt.setInt(1, gameId);
        stmt.setInt(2, userId);

        stmt.executeUpdate();
    }
}
