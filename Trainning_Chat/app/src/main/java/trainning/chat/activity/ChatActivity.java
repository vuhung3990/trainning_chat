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
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        emailfrom = MySharePreferences.getValue(this, "email", "");
//        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
//        reg_ID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
        messages = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        final String to = bundle.getString("to");
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
                            messages.add(new Message(message.getId(), message.getData(), message.getCreated_at(), true));
                            Log.d("CHAT-DATA", message.getUpdated_at());
                        }
                        Log.d("messages", messages.size() + "");
//                        mAdapter = new ChatAdapter(messages, mRcvChat);
//                        mRcvChat.setAdapter(mAdapter);
//                        mRcvChat.scrollToPosition(messages.size() - 1);

                        loadDataFirst();
                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
//                                Log.d("LOAD MORE", "touch scroll");
//                                loadlimit = MySharePreferences.getValue(getApplicationContext(), "limited", 0);
//                                loadMoreData(loadlimit + 10);
                                loadMoreDemo();

                            }
                        });
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
                client = new AsyncHttpClient();
                params = new RequestParams();
                params.put("from", emailfrom);
                params.put("to", to);
                params.put("token", token);
                params.put("type", "text");
                params.put("data", edtMessage.getText().toString());


                Log.d("ChatActivity Message", edtMessage.getText() + "");


//                params.put("reg_id", "APA91bFx7W0vQOC2gHoWkt9IZDXYj7gbvKxAs19FacwjYQkPt1FWYMjRCZn6BbXqfg5VBQjOzWkvRUvIeOqcA1EKsq7_JAmGMabIySw2YeAvPVjLkbVWZJ0");
                client.post(Utils.API_GCM_CHAT, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("ChatActivity chatstatus", statusCode + "__" + responseString);
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dateFormatter.setLenient(false);
                        Date today = new Date();
                        String s = dateFormatter.format(today);
                        messageFirst.add(new Message(1, edtMessage.getText().toString(), s, false));
                        mAdapter.notifyDataSetChanged();
                        edtMessage.setText(null);
                        mRcvChat.scrollToPosition(messageFirst.size() - 1);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("ChatActivity chatstatus", "post success");
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dateFormatter.setLenient(false);
                        Date today = new Date();
                        String s = dateFormatter.format(today);
//                        Log.d("CHAT TIME", s);

                        messageFirst.add(new Message(1, edtMessage.getText().toString(), s, true));
                        mAdapter.notifyDataSetChanged();
                        edtMessage.setText(null);
                        mRcvChat.scrollToPosition(messageFirst.size() - 1);
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


    @Subscribe
    public void getMessage(String msg) {
        if (msg != null) {
            showMessage(msg);
            Log.d("even bus msg", msg);
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

    private void showMessage(String msg) {

        messages.add(new Message(2, msg));
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
//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.d("LOAD MORE", "touch scroll");
//
//                loadMoreData(loadlimit);
//
//            }
//        });
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
}
