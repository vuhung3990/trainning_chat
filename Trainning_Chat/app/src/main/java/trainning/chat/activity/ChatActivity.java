package trainning.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import trainning.chat.R;
import trainning.chat.adapter.ChatAdapter;
import trainning.chat.entity.Message;

/**
 * Created by ASUS on 09/10/2015.
 */
public class ChatActivity extends Activity {

    private ArrayList<Message> messages;
    private ChatAdapter mAdapter;
    private RecyclerView mRcvChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mRcvChat = (RecyclerView) findViewById(R.id.tbChat);
        messages = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Message message = new Message();
            if ((i % 2) == 0) {

                message.setId(1);
                message.setMessage(" Tôi là Nguyên");
            } else {
                message.setId(2);
                message.setMessage("Tôi là Hùng");


            }
            messages.add(message);
            Log.d("FAKE ----------------->", messages.get(i).getMessage());

        }
        mAdapter = new ChatAdapter(this, messages);
        mRcvChat.setLayoutManager(new LinearLayoutManager(this));
        mRcvChat.setAdapter(mAdapter);
    }
}
