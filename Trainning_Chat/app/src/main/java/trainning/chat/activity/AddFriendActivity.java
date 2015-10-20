package trainning.chat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import trainning.chat.R;
import trainning.chat.preferences.MySharePreferences;

/**
 * Created by ASUS on 20/10/2015.
 */
public class AddFriendActivity extends Activity {

    private EditText edtEmail;
    private Button btnAddFr;
    String email;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend_activity);
        edtEmail = (EditText) findViewById(R.id.etEmail);
        btnAddFr = (Button) findViewById(R.id.btnAddFriend);
        email = MySharePreferences.getValue(this, "email", "");
        token = MySharePreferences.getValue(this, "token", "");
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setTitle("Please wait...");
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("owner", email);
                params.put("token", token);
                params.put("friend", edtEmail.getText().toString());
                client.post("http://trainningchat-vuhung3990.rhcloud.com/addFriend", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("Add Friend", responseString);
                        mDialog.dismiss();
                        Toast.makeText(AddFriendActivity.this, "Add Fail", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("Add Friend", responseString);
                        mDialog.dismiss();
                        Toast.makeText(AddFriendActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
            }
        });
    }
}
