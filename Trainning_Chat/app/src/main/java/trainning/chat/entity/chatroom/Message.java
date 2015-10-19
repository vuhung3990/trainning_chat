package trainning.chat.entity.chatroom;

import java.sql.Date;

/**
 * Created by ASUS on 14/10/2015.
 */
public class Message {
    private int id;
    private String message;
    private String time;
    private boolean status;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public Message(int id, String message, String time, boolean status) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
