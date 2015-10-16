package trainning.chat.entity;

import java.util.ArrayList;

/**
 * Created by ASUS on 12/10/2015.
 */
public class HistoryUser {
    ArrayList<HistoryUserData> data;

    public ArrayList<HistoryUserData> getData() {
        return data;
    }

    public void setData(ArrayList<HistoryUserData> data) {
        this.data = data;
    }
}