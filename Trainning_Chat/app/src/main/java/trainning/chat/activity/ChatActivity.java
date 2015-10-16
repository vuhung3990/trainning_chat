package trainning.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.adapter.ChatAdapter;
import trainning.chat.entity.Message;
import trainning.chat.gcm.GCMConfig;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mRcvChat = (RecyclerView) findViewById(R.id.tbChat);
        btnSend = (Button) findViewById(R.id.btnSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
//        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
//        reg_ID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
        messages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, messages);
        mRcvChat.setLayoutManager(new LinearLayoutManager(this));
        mRcvChat.setAdapter(mAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                token = MySharePreferences.getValue(ChatActivity.this, "token", "");
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
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
