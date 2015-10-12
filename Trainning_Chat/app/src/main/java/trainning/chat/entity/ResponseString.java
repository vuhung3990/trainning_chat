package trainning.chat.entity;

/**
 * Created by ASUS on 12/10/2015.
 */
public class ResponseString {
    public String action;
    public String status;
    public Data data;

    public ResponseString(Data data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public String getToken(){
        return data != null ? getData().getToken() : null;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Data {
    public String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Data(String token) {
        this.token = token;
    }
}
