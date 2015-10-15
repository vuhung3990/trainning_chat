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
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.adapter.ChatAdapter;
import trainning.chat.entity.Message;
import trainning.chat.gcm.GCMConfig;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mRcvChat = (RecyclerView) findViewById(R.id.tbChat);
        btnSend = (Button) findViewById(R.id.btnSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
        reg_ID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messages.add(new Message(1, edtMessage.getText().toString()));
                edtMessage.setText(null);
                mAdapter.notifyDataSetChanged();
                mRcvChat.scrollToPosition(messages.size() - 1);

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("text", edtMessage.getText().toString());
                params.put("reg_id", reg_ID);
                client.post("http://test101.grasys.us/test/gcm.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("aaa", statusCode + "__" + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("aaa", "post success");
                    }
                });


            }
        });


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
        mRcvChat.scrollToPosition(messages.size() - 1);
    }


    @Subscribe
    public void getMessage(String msg) {
        if (msg != null) {
            showMessage(msg);
        }

    }

    private void showMessage(String msg) {

        messages.add(new Message(2, msg));
        mAdapter.notifyDataSetChanged();
        mRcvChat.scrollToPosition(messages.size() - 1);

    }


}
