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
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import trainning.chat.activity.ChatActivity;
import trainning.chat.database.FixData;
import trainning.chat.R;
import trainning.chat.adapter.ContactAdapter;
import trainning.chat.activity.AddFriendActivity;
import trainning.chat.entity.contact.ContactUser;
import trainning.chat.entity.contact.Contacts;
import trainning.chat.entity.history.HistoryUserData;
import trainning.chat.preferences.MySharePreferences;

/**
 * Created by ASUS on 10/10/2015.
 */
public class ListUserFragment extends Fragment {
    private RecyclerView mRcvContact;
    private ArrayList<ContactUser> users;
    private ContactAdapter mAdapter;
    private Context mContext;
    private RecyclerView mRcvUser;
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

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", MySharePreferences.getValue(getActivity(), "email", ""));
        client.get("http://trainningchat-vuhung3990.rhcloud.com/friendList", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("CONTACT-LIST", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("CONTACT-LIST", responseString);
                if (responseString != null) {
                    Gson gson = new Gson();
                    Contacts contact = gson.fromJson(responseString, Contacts.class);
                    users = contact.getData();

                }
            }
        });


        mAdapter = new ContactAdapter(getActivity(), users, new ContactAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("to", users.get(position).getEmail());
                startActivity(intent);
            }
        });
        mRcvContact.setAdapter(mAdapter);

        return view;

    }
}
