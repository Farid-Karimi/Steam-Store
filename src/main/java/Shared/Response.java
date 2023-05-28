package Shared;

import java.io.File;
import java.io.Serializable;

public class Response implements Serializable {
    String json;
    private File file;

    public String getJson() {
        return json;
    }

    public File getFile() {
        return file;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private String message = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
