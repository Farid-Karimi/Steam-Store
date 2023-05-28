package Shared;

import Server.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserHandler {
    Database database;
    public UserHandler(Database database) {
        this.database = database;
    }
    public void createUser(User user) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement("INSERT INTO accounts (username, password, date_of_birth) " +
                "VALUES (?,?,?)");
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setDate(3, user.getDate());

        stmt.executeUpdate();
    }

    public User findUserByID(int id) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT * FROM accounts where id=?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if(rs.getRow() >= 0){
            rs.next();
            User res = new User();
            res.setId(rs.getInt("id"));
            res.setUsername(rs.getString("username"));
            res.setPassword(rs.getString("password"));
            res.setDate(rs.getDate("dateOfBirth"));
            return res;
        }
        return null;
    }

    public List<User> findUserByName(String username) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT * FROM accounts where username=?");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();

        List<User> res = new ArrayList<>();

        if(rs.getRow() >= 0){
            rs.next();
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setDate(rs.getDate("dateOfBirth"));
            res.add(user);
        }
        return res;
    }

    public boolean authenticatation(String username, String password) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT * FROM accounts where username=? and password=?");
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        return rs.getRow() == 1;
    }
}
