package trainning.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import trainning.chat.database.FixData;
import trainning.chat.R;
import trainning.chat.adapter.ContactAdapter;
import trainning.chat.activity.AddFriendActivity;
import trainning.chat.entity.contact.ContactUser;
import trainning.chat.entity.history.HistoryUserData;

/**
 * Created by ASUS on 10/10/2015.
 */
public class ListUserFragment extends Fragment {
    private RecyclerView mRcvContact;
    private ArrayList<ContactUser> users;
    private ContactAdapter mAdapter;
    private Context mContext;
    private RecyclerView mRcvUser;
    ArrayList<HistoryUserData> datas;
    private TextView tvAddFreind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_user_fragment, container, false);
        mRcvContact = (RecyclerView) view.findViewById(R.id.rcvContact);
        tvAddFreind = (TextView) view.findViewById(R.id.tvAddFreind);

        tvAddFreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
            }
        });

        mRcvContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        for (int i = 0; i < FixData.history_data_username.length; i++) {
            users.add(new ContactUser(FixData.history_data_username[i],
                    FixData.history_data_email[i]));
        }
        mAdapter = new ContactAdapter(getActivity(), users);
        mRcvContact.setAdapter(mAdapter);

        return view;

    }
}
