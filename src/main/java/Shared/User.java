package Shared;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class User implements Serializable {
    private String username;
    private String password;
    private Date date;
    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String parseDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        return sdf.format(this.getDate());
    }

    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String encryption(){
        return BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public void setDateWithText(String string){
        this.date = Date.valueOf(string);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date +
                '}';
    }
}
