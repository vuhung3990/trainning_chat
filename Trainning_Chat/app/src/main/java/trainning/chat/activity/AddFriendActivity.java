package trainning.chat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import trainning.chat.R;
import trainning.chat.util.MySharePreferences;
import trainning.chat.util.RequestUtils;

/**
 * Created by ASUS on 20/10/2015.
 */
public class AddFriendActivity extends Activity {

    private EditText edtEmail;
    private Button btnAddFr;
    String email;
    String token;
    public Pattern emailLegalPattern = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    private boolean loginID_isLegal = true;
    public final int USERNAME_MAX = 256;
    public final int USERNAME_MIN = 3;
    public final int PASSWORD_MIN = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend_activity);
        edtEmail = (EditText) findViewById(R.id.etEmail);
        btnAddFr = (Button) findViewById(R.id.btnAddFriend);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!emailLegalPattern.matcher(editable).matches()) {
                    loginID_isLegal = false;
                } else {
                    loginID_isLegal = true;
                }
            }
        });
        email = MySharePreferences.getValue(this, "email", "");
        token = MySharePreferences.getValue(this, "token", "");
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setTitle("Please wait...");
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!loginID_isLegal) {
                    if (edtEmail.length() > USERNAME_MAX) {
                        edtEmail.setError(">" + USERNAME_MAX);
                    } else if (edtEmail.length() < USERNAME_MIN) {
                        edtEmail.setError("<" + USERNAME_MIN);
                    } else {
                        edtEmail.setError("Email is wrong format");
                    }
                } else {
                    mDialog.show();
                    RequestUtils.addFriend(email, token, edtEmail.getText().toString(), new RequestUtils.addFriendCallback() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("Add Friend", responseString);
                            mDialog.dismiss();
                            Toast.makeText(AddFriendActivity.this, "Add Fail, Email had in contact or not exist", Toast.LENGTH_SHORT).show();
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


            }
        });


    }
}
