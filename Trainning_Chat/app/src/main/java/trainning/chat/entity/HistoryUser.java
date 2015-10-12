package trainning.chat.entity;

/**
 * Created by ASUS on 12/10/2015.
 */
public class HistoryUser {
    private String username;
    private String email;
    private String message_latter;
    private String time;

    public HistoryUser(String username, String message_latter, String time) {
        this.username = username;
        this.message_latter = message_latter;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage_latter() {
        return message_latter;
    }

    public void setMessage_latter(String message_latter) {
        this.message_latter = message_latter;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
