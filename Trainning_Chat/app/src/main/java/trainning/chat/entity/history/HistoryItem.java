package trainning.chat.entity.history;

/**
 * Created by NGUYEN on 10/22/2015.
 */
public class HistoryItem {
    private String email;
    private String message_last;
    private String time;

    private boolean flag_inbox;

    public HistoryItem(String email, String message_last, String time, boolean flag_inbox) {
        this.email = email;
        this.message_last = message_last;
        this.time = time;
        this.flag_inbox = flag_inbox;
    }

    public boolean isFlag_inbox() {
        return flag_inbox;
    }

    public void setFlag_inbox(boolean flag_inbox) {
        this.flag_inbox = flag_inbox;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage_last() {
        return message_last;
    }

    public void setMessage_last(String message_last) {
        this.message_last = message_last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
