package trainning.chat.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import trainning.chat.activity.ChatActivity;
import trainning.chat.R;
import trainning.chat.activity.HomeActivity;
import trainning.chat.activity.LogInActivity;
import trainning.chat.adapter.ContactAdapter;
import trainning.chat.activity.AddFriendActivity;
import trainning.chat.entity.contact.ContactUser;
import trainning.chat.entity.contact.Contacts;
import trainning.chat.util.MySharePreferences;
import trainning.chat.util.RequestUtils;
import trainning.chat.util.Utils;

/**
 * Created by ASUS on 10/10/2015.
 */
public class ListUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, HomeActivity.SearchOnBackPessListener {
    private RecyclerView mRcvContact;
    private ArrayList<ContactUser> users;
    private ContactAdapter mAdapter;
    private Context mContext;
    private RecyclerView mRcvUser;
    private TextView tvAddFreind;
    private SearchView mSearchView;
    private List<ContactUser> saveListUserForSearchView = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String myEmail;
    public static boolean search = false;
    private ProgressDialog mDialog;
    String email;
    String token;
    private boolean addNewFr = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_user_fragment, container, false);
        mRcvContact = (RecyclerView) view.findViewById(R.id.rcvContact);
        tvAddFreind = (TextView) view.findViewById(R.id.tvAddFreind);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(this);
//        mSearchView.onActionViewExpanded();
//        mSearchView.onActionViewCollapsed();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvAddFreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
            }
        });

        mRcvContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDialog = new ProgressDialog(getActivity());
        mDialog.show();
        users = new ArrayList<>();
        myEmail = MySharePreferences.getValue(getActivity(), "email", "");

        getContact(myEmail);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = true;
                users.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search = false;
                users.clear();
                if (!addNewFr) {

                    users.addAll(saveListUserForSearchView);
                    mAdapter.notifyDataSetChanged();
                } else {

                    RequestUtils.getContactList(myEmail, new RequestUtils.contactCallback() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            getContact(myEmail);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            getContact(myEmail);
                        }
                    });
                    addNewFr = false;
                }
                return false;

            }

        });
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_BACK) {
//                    if (search) {
//                        mSearchView.onActionViewCollapsed();
//                    }
//                }
//
//                return false;
//            }
//        });
        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CONTACT-RESUME", "On Resume");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        String textInput = s.trim();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("keyword", textInput);
        client.get(Utils.API_SEARCH, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                swipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                Contacts contact = gson.fromJson(responseString, Contacts.class);
                users.clear();
                users.addAll(contact.getData());
//                Log.d("SEARCH-LIST", users.get(0).getEmail());
                mAdapter.notifyDataSetChanged();
            }
        });


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public void getContact(String my_email) {
        RequestUtils.getContactList(my_email, new RequestUtils.contactCallback() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
//                Log.d("CONTACT-LIST", responseString);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dismissProgressDialog();
                Log.d("CONTACT-LIST", responseString);
                swipeRefreshLayout.setRefreshing(false);
                if (responseString != null) {
                    Gson gson = new Gson();
                    Contacts contact = gson.fromJson(responseString, Contacts.class);
                    saveListUserForSearchView.clear();
                    users = contact.getData();

                    saveListUserForSearchView.addAll(users);

                    Log.d("CONTACT-SIZE", users.size() + "");
                    mAdapter = new ContactAdapter(getContext(), users, new ContactAdapter.OnItemClickListener() {
                        @Override
                        public void setOnItemClick(int position) {
                            Log.d("CONTACT-POSSTION", position + "");
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("to", users.get(position).getEmail());
                            startActivity(intent);
                        }


                        @Override
                        public void setOnItemLongClick(final int position) {
                            if (search) {
                                if (users.get(position).getEmail() != myEmail) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage("Add Friend ?");

                                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            email = MySharePreferences.getValue(getActivity(), "email", "");
                                            token = MySharePreferences.getValue(getActivity(), "token", "");
                                            dismissProgressDialog();
                                            if (email.equals(users.get(position).getEmail())) {
                                                Toast.makeText(getActivity(), "Add Fail", Toast.LENGTH_SHORT).show();
                                            } else {
                                                RequestUtils.addFriend(email, token, users.get(position).getEmail(), new RequestUtils.addFriendCallback() {
                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                        Log.d("Add Friend", responseString);
                                                        mDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Add Fail, Email had in contact or not exist", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                                                        addNewFr = true;
                                                        Log.d("Add Friend", responseString);
                                                        dismissProgressDialog();
                                                        Toast.makeText(getActivity(), "Add Success", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }

                                        }
                                    });

                                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }


                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setMessage("Un Friend ?");

                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        email = MySharePreferences.getValue(getActivity(), "email", "");
                                        token = MySharePreferences.getValue(getActivity(), "token", "");
                                        Log.d("Un Friend", "Email" + email + "");
                                        Log.d("Un Friend", "token" + token + "");
                                        Log.d("Un Friend", "Friend" + users.get(position).getEmail() + "");
                                        mDialog.show();
                                        RequestUtils.unFriend(email, users.get(position).getEmail(), token, new RequestUtils.unFriendCallback() {
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                Log.d("Un Friend", statusCode + "--------" + responseString + "");
                                                dismissProgressDialog();
                                                Toast.makeText(getActivity(), "UnFriend not success, please check network", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                Log.d("Un Friend", responseString + "");
                                                users.remove(position);
                                                mAdapter.notifyDataSetChanged();
                                                dismissProgressDialog();
                                                Toast.makeText(getActivity(), "UnFriend Success", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }


                                });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                            }
                        }
                    }

                    );
                    mRcvContact.setAdapter(mAdapter);
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        users.clear();
        mAdapter.notifyDataSetChanged();
        getContact(myEmail);
    }


    private void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void searchOnbackListener() {
        mSearchView.onActionViewCollapsed();
        users.clear();
        users.addAll(saveListUserForSearchView);
        mAdapter.notifyDataSetChanged();
        search = false;


    }
}
