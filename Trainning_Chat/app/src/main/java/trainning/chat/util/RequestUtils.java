package trainning.chat.util;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import trainning.chat.activity.ChatActivity;
import trainning.chat.activity.HomeActivity;
import trainning.chat.adapter.ContactAdapter;
import trainning.chat.adapter.HistoryAdapter;
import trainning.chat.entity.ResponseString;
import trainning.chat.entity.contact.Contacts;
import trainning.chat.entity.history.HistoryUser;
import trainning.chat.entity.history.HistoryUserData;

/**
 * Created by ASUS on 21/10/2015.
 */
public class RequestUtils {

    public static void logIn(String email, String password, String regID, final logInCallback logInCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("reg_id", regID);
        client.post(Utils.API_LOGIN, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logInCallback.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                logInCallback.onSuccess(statusCode, headers, responseString);
            }
        });

    }

    public static void autoLogIn(String email, String token, String regID, final logInCallback logInCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("token", token);
        params.put("reg_id", regID);
        client.post(Utils.API_LOGIN, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logInCallback.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                logInCallback.onSuccess(statusCode, headers, responseString);
            }
        });

    }

    public static void register(String email, String userName, String password, final registerCallback registerCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("name", userName);
        params.put("password", password);
        client.post(Utils.API_REGISTER, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                registerCallback.onFailure(statusCode, headers, responseString, throwable);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                registerCallback.onSuccess(statusCode, headers, responseString);
            }
        });


    }

    public static void getHistoryList(String email, final historyCallback historyCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        client.get(Utils.API_CHAT_HISTORY, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                historyCallback.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                historyCallback.onSuccess(statusCode, headers, responseString);
            }
        });


    }

    public static void getContactList(String email, final contactCallback contactCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        client.get(Utils.API_CONTACT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                contactCallback.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                contactCallback.onSuccess(statusCode, headers, responseString);
            }
        });

    }

    public static void addFriend(String email, String token, String emailFriend, final addFriendCallback addFriendCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("owner", email);
        params.put("token", token);
        params.put("friend", emailFriend);
        client.post("http://trainningchat-vuhung3990.rhcloud.com/addFriend", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                addFriendCallback.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                addFriendCallback.onSuccess(statusCode, headers, responseString);
            }
        });

    }


    public interface logInCallback {
        void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        void onSuccess(int statusCode, Header[] headers, String responseString);
    }

    public interface registerCallback {
        void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        void onSuccess(int statusCode, Header[] headers, String responseString);

    }

    public interface historyCallback {
        void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        void onSuccess(int statusCode, Header[] headers, String responseString);
    }


    public interface contactCallback {
        void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        void onSuccess(int statusCode, Header[] headers, String responseString);
    }

    public interface addFriendCallback {
        void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        void onSuccess(int statusCode, Header[] headers, String responseString);
    }
}
