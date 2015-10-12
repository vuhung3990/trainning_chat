package trainning.chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trainning.chat.FixData;
import trainning.chat.R;
import trainning.chat.adapter.HistoryAdapter;
import trainning.chat.entity.HistoryUser;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HistoryFragment extends Fragment {

    private ListView mLvUser;
    private ArrayList<HistoryUser> users;
    private HistoryAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mLvUser = (ListView) view.findViewById(R.id.lvHistory);
        users = new ArrayList<>();
        for (int i = 0; i < FixData.history_data_username.length; i++) {
            users.add(new HistoryUser(FixData.history_data_username[i],
                    FixData.history_data_message_latter[i], FixData.history_data_time[i]));
        }
        mAdapter = new HistoryAdapter(getActivity(), users);
        mLvUser.setAdapter(mAdapter);

        return view;

    }
}
