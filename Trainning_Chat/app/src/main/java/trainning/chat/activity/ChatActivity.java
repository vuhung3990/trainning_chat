package trainning.chat.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.adapter.ChatAdapter;
import trainning.chat.entity.chatroom.DataChat;
import trainning.chat.entity.chatroom.Message;
import trainning.chat.entity.chatroom.MessageChat;
import trainning.chat.entity.chatroom.OnLoadMoreListener;
import trainning.chat.gcm.GCMConfig;
import trainning.chat.gcm.GCMNotificationIntentService;
import trainning.chat.util.MySharePreferences;
import trainning.chat.util.Utils;

/**
 * Created by ASUS on 09/10/2015.
 */
public class ChatActivity extends Activity {
    private HashMap<Integer, ArrayList<Message>> map = new HashMap<>();
    private ArrayList<ArrayList<Message>> arrtong = new ArrayList<>();
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
    private ArrayList<Message> message_byTimes = new ArrayList<>();
    private ChatAdapter[] arrAdapter;
    private int count[];
    private ArrayList<Message> messageFirst = new ArrayList<>();
    private int loadlimit;
    private android.os.Handler mHandler = new android.os.Handler();
    private int k = 0;
    private TextView tvTitle;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private String message_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mRcvChat = (RecyclerView) findViewById(R.id.tbChat);
        mRcvChat.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
        mRcvChat.setLayoutManager(layoutManager);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        emailfrom = MySharePreferences.getValue(this, "email", "");
//        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
//        reg_ID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
        messages = new ArrayList<>();
        mAdapter = new ChatAdapter(messages, mRcvChat);
        mRcvChat.setAdapter(mAdapter);
        Bundle bundle = getIntent().getExtras();
        final String to = bundle.getString("to");
        tvTitle.setText("Chat with: " + to);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);

        client = new AsyncHttpClient();
        params = new RequestParams();
        params.put("from", emailfrom);
        Log.d("Email From", emailfrom);
        params.put("to", to);
        Log.d("Email To", to);
        client.get(Utils.API_CHAT_ROOM, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (responseString != null)
                    Log.d("Send message Fail", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("Message History", responseString);

                if (responseString != null) {
                    Gson gson = new Gson();
                    DataChat dataChat = gson.fromJson(responseString, DataChat.class);
                    messageChats = dataChat.getData();
//                    Collections.reverse(messageChats);
                    if (messageChats.size() > 0) {
                        for (MessageChat message : messageChats) {

                            if (message.getFrom().equals(emailfrom)) {

                                message.setId(1);

                            } else {
                                message.setId(2);
                            }
                            messages.add(new Message(message.getId(), message.getData(), message.getCreated_at(), Utils.KEY_SEND_SUCCESS));
                            Log.d("CHAT-DATA", message.getUpdated_at());
                        }
                        Collections.reverse(messages);


                        Log.d("messages", messages.size() + "");
                        mAdapter.notifyDataSetChanged();
                        mRcvChat.scrollToPosition(messages.size() - 1);

//                        loadDataFirst();
//                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//                            @Override
//                            public void onLoadMore() {
////                                Log.d("LOAD MORE", "touch scroll");
////                                loadlimit = MySharePreferences.getValue(getApplicationContext(), "limited", 0);
////                                loadMoreData(loadlimit + 10);
//                                loadMoreDemo();
//
//                            }
//                        });
//                        for (int i = 0; i < messages.size() - 1; i++) {
//                            Log.d("TIME---", messages.get(i).getTime());
//
//                            Date date1 = null;
//                            Date date2 = null;
//                            try {
//                                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i).getTime());
//                                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i + 1).getTime());
//                                String newString1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
//                                String newString2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
//                                if (newString1.equals(newString2)) {
//                                    message_byTimes.add(messages.get(i));
//                                    if (i == messages.size() - 2) {
//                                        message_byTimes.add(messages.get(i + 1));
//                                        arrtong.add(message_byTimes);
//                                    }
//                                } else {
//                                    message_byTimes.add(messages.get(i));
//                                    arrtong.add(message_byTimes);
//                                    message_byTimes = new ArrayList<Message>();
//
//
//                                }
//
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    startDate = df.parse(startDateString);


//                        for (int i = 0; i < arrtong.size(); i++) {
//                            Log.d("ArrTong.SIZE", arrtong.get(i).size() + "");
//                        }


//                    arrAdapter = new ChatAdapter[messages.size()];


                    }
                }
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                token = MySharePreferences.getValue(ChatActivity.this, "token", "");

                Log.d("ChatAc TOKEN SEND", token);
                message_input = edtMessage.getText().toString();
                client = new AsyncHttpClient();
                params = new RequestParams();
                params.put("from", emailfrom);
                params.put("to", to);
                params.put("token", token);
                params.put("type", "text");
                params.put("data", message_input);
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String s = dateFormatter.format(today);
                messages.add(new Message(1, message_input, s, Utils.KEY_SEND_SENDING));
                mAdapter.notifyDataSetChanged();
                edtMessage.setText(null);
                mRcvChat.scrollToPosition(messages.size() - 1);

                Log.d("ChatActivity Message", edtMessage.getText() + "");

//                params.put("reg_id", "APA91bFx7W0vQOC2gHoWkt9IZDXYj7gbvKxAs19FacwjYQkPt1FWYMjRCZn6BbXqfg5VBQjOzWkvRUvIeOqcA1EKsq7_JAmGMabIySw2YeAvPVjLkbVWZJ0");
                client.post(Utils.API_GCM_CHAT, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("ChatActivity chatstatus", statusCode + "__" + responseString);
                        if (statusCode == 406) {
                            Toast.makeText(getApplicationContext(), "Send message fail , Acount not activated", Toast.LENGTH_SHORT).show();
                        }


                        messages.get(messages.size() - 1).setStatus(Utils.KEY_SEND_FAIL);
                        mAdapter.notifyDataSetChanged();
                        mRcvChat.scrollToPosition(messages.size() - 1);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("ChatActivity chatstatus", responseString);
                        messages.get(messages.size() - 1).setStatus(Utils.KEY_SEND_SUCCESS);
                        mAdapter.notifyDataSetChanged();
                    }
                });


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Subscribe
    public void getMessage(MessageChat msg) {

        if (msg != null && msg.getTo().equals(emailfrom)) {
            showMessage(msg.getData().toString(), msg.getCreated_at().toString());
//            Log.d("even bus msg", msg);
        } else if (msg != null && msg.getAction() != null && msg.getEmail().equals(emailfrom)) {
            closeApp();
        }

    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        Log.d("eventbus register", "register");
        isrunning = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        Log.d("eventbus register", "unregister");
        isrunning = false;
        super.onPause();
    }

    private void showMessage(String msg, String date) {

        messages.add(new Message(2, msg, date, Utils.KEY_SEND_SUCCESS));
        mAdapter.notifyDataSetChanged();
        mRcvChat.scrollToPosition(messages.size() - 1);

    }

    public void loadData() {

        for (int i = 0; i < messages.size() - 1; i++) {
//            Log.d("TIME---", messages.get(i).getTime());

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i).getTime());
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i + 1).getTime());
                String newString1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                String newString2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
                if (newString1.equals(newString2)) {
                    message_byTimes.add(messages.get(i));
                    break;

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public void loadDataFirst() {
//        Collections.reverse(messages);
        for (int i = 0; i < messages.size() - 1; i++) {
//            Log.d("TIME---", messages.get(i).getTime());

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i).getTime());
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messages.get(i + 1).getTime());
                String newString1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                String newString2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
                if (!newString1.equals(newString2)) {
                    loadlimit = i;
                    break;

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < loadlimit; i++) {
            messageFirst.add(messages.get(i));
            Log.d("TIME---", messages.get(i).getTime());
        }

        Collections.reverse(messageFirst);
        Log.d("messageFirst ", messageFirst.size() + "");
        mAdapter = new ChatAdapter(messageFirst, mRcvChat);

        mRcvChat.setAdapter(mAdapter);
        mRcvChat.scrollToPosition(messageFirst.size() - 1);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("LOAD MORE", "touch scroll");
//                                loadlimit = MySharePreferences.getValue(getApplicationContext(), "limited", 0);
//                                loadMoreData(loadlimit + 10);
                loadMoreDemo();

            }
        });
    }

    public void loadMoreDemo() {

        messageFirst.add(0, null);
//        Collections.reverse(messageFirst);
        mAdapter.notifyItemInserted(0);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                messageFirst.remove(0);
                mAdapter.notifyItemRemoved(0);
                int start = messageFirst.size();
                int end = start + 10;
                if (end < messages.size()) {
                    for (int i = 0; i < end; i++) {
                        messageFirst.add(messages.get(i));

                    }
                } else {
                    return;
                }
                Collections.reverse(messageFirst);
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();

            }
        }, 2000);


    }


    public void loadMoreData(int limit) {
        Log.d("LOAD LIMIT--------", limit + "");

        message_byTimes.clear();
        for (int i = limit + 1; i < messages.size() - 1; i++) {
//            Log.d("TIME---", messages.get(i).getTime());

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(messages.get(i).getTime());
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(messages.get(i + 1).getTime());
                String newString1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                String newString2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);

//                date1.compareTo(date2)
                if (newString1.equals(newString2)) {
                    limit++;
                    message_byTimes.add(messages.get(i));

                    Log.d("LOAD LIMIT", limit + "");


                } else {

                    break;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        k = limit;
        Log.d("message_byTimes", message_byTimes.size() + "");
        Collections.reverse(message_byTimes);
        messageFirst.add(0, null);
//        Collections.reverse(messageFirst);
        mAdapter.notifyItemInserted(0);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageFirst.remove(0);
                mAdapter.notifyItemRemoved(0);

                messageFirst.addAll(0, message_byTimes);
                Log.d("messageFirst load more", messageFirst.size() + "");
                mAdapter.notifyItemInserted(messageFirst.size());
//                mRcvChat.scrollToPosition(k);
//                Collections.reverse(messageFirst);

                mAdapter.setLoaded();

            }
        }, 2000);
        MySharePreferences.setValue(getApplicationContext(), "limited", limit);
//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//                Log.d("KKKKKKKKKK", k + "");
//                loadMoreData(k);
//            }
//        });

    }

    public void closeApp() {
        Toast.makeText(getApplicationContext(), "App Closed, Account was logged by other devices ", Toast.LENGTH_LONG).show();
        MySharePreferences.setValue(this, "email", null);
        MySharePreferences.setValue(this, "token", null);
        MySharePreferences.setValue(this, "checked", false);
        mSharedPreferences.edit().putString(GCMConfig.PREFERENCE_KEY_REG_ID, null).commit();

        finish();
        startActivity(new Intent(this, SplashActivity.class));
    }
}
