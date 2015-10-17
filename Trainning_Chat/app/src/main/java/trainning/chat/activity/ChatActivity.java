package trainning.chat.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.adapter.ChatAdapter;
import trainning.chat.entity.chatroom.DataChat;
import trainning.chat.entity.Message;
import trainning.chat.entity.chatroom.MessageChat;
import trainning.chat.preferences.MySharePreferences;

/**
 * Created by ASUS on 09/10/2015.
 */
public class ChatActivity extends Activity {


    private ArrayList<Message> messages;
    private ChatAdapter mAdapter;
    private RecyclerView mRcvChat;
    private Button btnSend;
    private EditText edtMessage;
    private SharedPreferences mSharedPreferences;
    private String reg_ID;
    private int max = 100;
    public static boolean isrunning;
    public String token;
    AsyncHttpClient client;
    RequestParams params;
    private ArrayList<MessageChat> messageChats = new ArrayList<>();
    private String emailfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mRcvChat = (RecyclerView) findViewById(R.id.tbChat);
        btnSend = (Button) findViewById(R.id.btnSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        emailfrom = MySharePreferences.getValue(this, "email", "");
//        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
//        reg_ID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
        messages = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String to = bundle.getString("to");
        client = new AsyncHttpClient();
        params = new RequestParams();
        params.put("from", emailfrom);
        params.put("to", to);
        client.get("http://trainningchat-vuhung3990.rhcloud.com/roomChatHistory", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString != null) {
                    Gson gson = new Gson();
                    DataChat dataChat = gson.fromJson(responseString, DataChat.class);
                    messageChats = dataChat.getData();
                    for (MessageChat message : messageChats) {
                        if (message.getFrom().equals(emailfrom)) {
                            message.setId(1);
                        } else {
                            message.setId(2);
                        }
                        messages.add(new Message(message.getId(), message.getData()));

                    }
                    mAdapter = new ChatAdapter(ChatActivity.this, messages);
                    mRcvChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    mRcvChat.setAdapter(mAdapter);
                    mRcvChat.scrollToPosition(messages.size() - 1);
                }

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                token = MySharePreferences.getValue(ChatActivity.this, "token", "");
                client = new AsyncHttpClient();
                params = new RequestParams();
                params.put("from", "test@gmail.com");
                params.put("to", "nguyen.gemtek@gmail.com");
                params.put("token", token);
                params.put("type", "text");
                params.put("data", edtMessage.getText().toString());


                Log.d("CONTENT", edtMessage.getText() + "");


//                params.put("reg_id", "APA91bFx7W0vQOC2gHoWkt9IZDXYj7gbvKxAs19FacwjYQkPt1FWYMjRCZn6BbXqfg5VBQjOzWkvRUvIeOqcA1EKsq7_JAmGMabIySw2YeAvPVjLkbVWZJ0");
                client.post("http://trainningchat-vuhung3990.rhcloud.com/chat", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("aaa", statusCode + "__" + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("aaa", "post success");
                    }
                });

                messages.add(new Message(1, edtMessage.getText().toString()));
                mAdapter.notifyDataSetChanged();
                edtMessage.setText(null);
                mRcvChat.scrollToPosition(messages.size() - 1);
            }
        });


//        for (int i = 0; i < 100; i++) {
//            Message message = new Message();
//            if ((i % 2) == 0) {
//
//                message.setId(1);
//                message.setMessage(" Tôi là Nguyên");
//            } else {
//                message.setId(2);
//                message.setMessage("Tôi là Hùng");
//
//
//            }
//            messages.add(message);
//            Log.d("FAKE ----------------->", messages.get(i).getMessage());
//
//        }

    }


    @Subscribe
    public void getMessage(String msg) {
        if (msg != null) {
            showMessage(msg);
            Log.d("msg", msg);
        }

    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        Log.d("aaaa", "register");
        isrunning = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        Log.d("aaaa", "unregister");
        isrunning = false;
        super.onPause();
    }

    private void showMessage(String msg) {

        messages.add(new Message(2, msg));
        mAdapter.notifyDataSetChanged();
        mRcvChat.scrollToPosition(messages.size() - 1);

    }


}
