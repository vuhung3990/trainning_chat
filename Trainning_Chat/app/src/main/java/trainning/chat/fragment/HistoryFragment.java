package trainning.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import trainning.chat.R;
import trainning.chat.activity.ChatActivity;
import trainning.chat.adapter.HistoryAdapter;
import trainning.chat.entity.history.HistoryUser;
import trainning.chat.entity.history.HistoryUserData;
import trainning.chat.preferences.MySharePreferences;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HistoryFragment extends Fragment {
    private Context mContext;
    private RecyclerView mRcvUser;
    private ArrayList<HistoryUser> users;
    private HistoryAdapter mAdapter;
    ArrayList<HistoryUserData> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mRcvUser = (RecyclerView) view.findViewById(R.id.rcvHistory);
        mRcvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", MySharePreferences.getValue(getActivity(), "email", ""));
        client.get("http://trainningchat-vuhung3990.rhcloud.com/chatHistory", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                HistoryUser content = gson.fromJson(responseString, HistoryUser.class);
                datas = new ArrayList<HistoryUserData>();
                datas = content.getData();

                for (HistoryUserData data : datas) {
                    Log.d("DATAA", data.getEmail() + "");
                }

                mAdapter = new HistoryAdapter(getActivity(), datas);
                mRcvUser.setAdapter(mAdapter);
                mAdapter.setItemClickListener(new HistoryAdapter.OnItemClickListener() {
                    @Override
                    public void setonItemClick(View view, int position) {

                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("to", datas.get(position).getEmail());
                        startActivity(intent);

                    }

                });

            }
        });

//        for (int i = 0; i < FixData.history_data_username.length; i++) {
//            users.add(new HistoryUser(FixData.history_data_username[i],
//                    FixData.history_data_message_latter[i], FixData.history_data_time[i]));
//        }


        return view;

    }


}
