package trainning.chat.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.activity.ChatActivity;
import trainning.chat.adapter.HistoryAdapter;
import trainning.chat.entity.chatroom.Message;
import trainning.chat.entity.chatroom.MessageChat;
import trainning.chat.entity.history.HistoryItem;
import trainning.chat.entity.history.HistoryUser;
import trainning.chat.entity.history.HistoryUserData;
import trainning.chat.util.MySharePreferences;
import trainning.chat.util.RequestUtils;
import trainning.chat.util.Utils;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private RecyclerView mRcvUser;
    //    private ArrayList<HistoryUser> users;
    private HistoryAdapter mAdapter;
    ArrayList<HistoryUserData> datas;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HistoryItem> users;
    private MessageChat message;
    String email;
    private ProgressDialog mDialog;

    public void setMessage(MessageChat message) {
        this.message = message;
    }

    public MessageChat getMessage() {
        return message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRcvUser = (RecyclerView) view.findViewById(R.id.rcvHistory);
        email = MySharePreferences.getValue(getActivity(), "email", "");
        mRcvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
//        users = new ArrayList<>();
        users = new ArrayList<>();
        mAdapter = new HistoryAdapter(getActivity(), users);
        mRcvUser.setAdapter(mAdapter);
        mDialog = new ProgressDialog(getActivity());
        mDialog.show();

        getHistory();
        return view;

    }

    @Subscribe
    public void getMessage(MessageChat msg) {
        setMessage(msg);
        if (msg != null && msg.getTo() != null && msg.getTo().equals(email)) {
            showMessageNew(msg);
        }

    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        Log.d("eventbus register", "register");
//        getHistory();
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        Log.d("eventbus register", "unregister");
        super.onPause();
    }

    public void getHistory() {


        RequestUtils.getHistoryList(email, new RequestUtils.historyCallback() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Get list Fail,please check network", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                HistoryUser content = gson.fromJson(responseString, HistoryUser.class);
                datas = new ArrayList<HistoryUserData>();
                datas = content.getData();

                for (HistoryUserData data : datas) {
                    users.add(new HistoryItem(data.getEmail(), data.getData(), data.getCreated_at().getDate(), false));

                    Log.d("History list DATA", data.getEmail() + "");
                }
                mAdapter.notifyDataSetChanged();
//                mAdapter = new HistoryAdapter(getActivity(), users);
//                mRcvUser.setAdapter(mAdapter);
//                Log.d("MESSAGE", getMessage().getFrom() + "");
//                Log.d("USER", users.get(position).getEmail() + "");

                mAdapter.setItemClickListener(new HistoryAdapter.OnItemClickListener() {
                    @Override
                    public void setonItemClick(View view, int position) {
                        Log.d("POSSITION", position + "");
//                        Log.d("MESSAGE", getMessage().getFrom() + "");
//                        Log.d("USER", users.get(position).getEmail() + "");
                        if (getMessage() != null) {
                            if (users.get(position).getEmail().equals(getMessage().getFrom())) {
                                users.get(position).setFlag_inbox(false);

                                mAdapter.notifyDataSetChanged();

                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                intent.putExtra("to", users.get(position).getEmail());
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("to", users.get(position).getEmail());
                            startActivity(intent);
                        }
                    }

                });
            }
        });

    }

    public void showMessageNew(MessageChat message) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(message.getFrom())) {
                users.get(i).setFlag_inbox(true);
                mAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        users.clear();
        mAdapter.notifyDataSetChanged();
        getHistory();
    }
}
