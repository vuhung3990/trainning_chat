package trainning.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import trainning.chat.database.FixData;
import trainning.chat.R;
import trainning.chat.activity.ChatActivity;
import trainning.chat.adapter.HistoryAdapter;
import trainning.chat.entity.HistoryUser;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView mRcvUser;
    private ArrayList<HistoryUser> users;
    private HistoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mRcvUser = (RecyclerView) view.findViewById(R.id.rcvHistory);
        mRcvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        for (int i = 0; i < FixData.history_data_username.length; i++) {
            users.add(new HistoryUser(FixData.history_data_username[i],
                    FixData.history_data_message_latter[i], FixData.history_data_time[i]));
        }
        mAdapter = new HistoryAdapter(getActivity(), users);
        mRcvUser.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void setonItemClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        return view;

    }


}
