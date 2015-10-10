package trainning.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ASUS on 09/10/2015.
 */
public class LogInActivity extends AppCompatActivity {
    private EditText mEdtEmail, mEdtpass;
    private Button mBtnLogIn;
    private TextView mTvForgotPassword, mTvCreateAccount;
    private User mUser;
    private DatabaseHandler mDatabaseHandler;
    private Toolbar toolbar;
    private boolean loginID_isLegal = true;
    private boolean loginPW_isLegal = true;
    public final int USERNAME_MAX = 256;
    public final int USERNAME_MIN = 3;
    public final int PASSWORD_MIN = 8;
    public Pattern usernameLegalPattern = Pattern.compile("^[a-zA-Z0-9_.-@]+$");
    public Pattern emailLegalPattern = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mEdtEmail = (EditText) findViewById(R.id.etEmail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        setSupportActionBar(toolbar);
        mTvCreateAccount = (TextView) findViewById(R.id.tvCreateAccount);
        mTvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        mTvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        mTvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });
//        mDatabaseHandler = new DatabaseHandler(this);
        mEdtpass = (EditText) findViewById(R.id.etPassword);
        mEdtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEdtEmail.setError(null);
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

        mEdtpass.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                mEdtpass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdtpass.length() < PASSWORD_MIN) {
                    // mLoginPassword.setError("< "+PASSWORD_MIN);
                    loginPW_isLegal = false;
                } else {
                    // mLoginPassword.setError(null);
                    loginPW_isLegal = true;
                }
            }

        });

        mBtnLogIn = (Button) findViewById(R.id.btnLogIn);
        mBtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(LogInActivity.this);
                mDialog.setTitle("Loging....");
                mDialog.show();

                String email = mEdtEmail.getText().toString();
                String password = mEdtpass.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Can not be blank", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (loginID_isLegal && loginPW_isLegal) {
//                    mUser = mDatabaseHandler.getUser(email);
//                    if (email.equals(mUser.getEmail()) && password.equals(mUser.getPassWord())) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("email", email);
                    params.put("password", password);
                    client.post("http://trainningchat-vuhung3990.rhcloud.com/login", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("STATUS CODE", statusCode + "");
                            Log.d("RESPONSE STRING", responseString + "");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.d("STATUS CODE", statusCode + "");
                            Log.d("RESPONSE STRING", responseString + "");
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            mDialog.dismiss();

                        }
                    });
//                    }


                } else {
                    if (!loginID_isLegal) {
                        if (mEdtEmail.length() > USERNAME_MAX) {
                            mEdtEmail.setError(">" + USERNAME_MAX);
                        } else if (mEdtEmail.length() < USERNAME_MIN) {
                            mEdtEmail.setError("<" + USERNAME_MIN);

                        } else {
                            mEdtEmail.setError("Email not match");
                        }
                    }
                    if (!loginPW_isLegal) {
                        if (mEdtpass.length() < PASSWORD_MIN) {
                            mEdtpass.setError("<" + PASSWORD_MIN);
                        }
                    }
                }

            }
        });
    }
}
