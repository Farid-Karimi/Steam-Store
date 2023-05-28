package Shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String command;
    private ArrayList<String> arr = null;
    private User user = null;
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    String sessionID = null;

    public String getCommand() {
        return command;
    }

    public String getSessionID() {
        return sessionID;
    }

    public Request(String command) {
        this.command = command;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    public void setArr(ArrayList<String> arr) {
        this.arr = arr;
    }

    public User getUser() {
        return user;
    }
}
