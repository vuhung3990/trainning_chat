package trainning.chat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import trainning.chat.R;
import trainning.chat.database.DatabaseHandler;
import trainning.chat.entity.ResponseString;
import trainning.chat.entity.User;
import trainning.chat.gcm.GCMConfig;
import trainning.chat.util.MySharePreferences;
import trainning.chat.util.RequestUtils;

/**
 * Created by ASUS on 09/10/2015.
 */
public class LogInActivity extends AppCompatActivity {
    private EditText mEdtEmail, mEdtpass;
    private Button mBtnLogIn;
    private TextView mTvForgotPassword, mTvCreateAccount;
    private CheckBox cbKeepMeSigin;
    private User mUser;
    private DatabaseHandler mDatabaseHandler;
    private SharedPreferences mSharedPreferences;
    private Toolbar toolbar;
    private boolean loginID_isLegal = true;
    private boolean loginPW_isLegal = true;
    public final int USERNAME_MAX = 256;
    public final int USERNAME_MIN = 3;
    public final int PASSWORD_MIN = 8;
    public String regID;
    public String token;
    public Pattern usernameLegalPattern = Pattern.compile("^[a-zA-Z0-9_.-@]+$");
    public Pattern emailLegalPattern = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mEdtEmail = (EditText) findViewById(R.id.etEmail);
        cbKeepMeSigin = (CheckBox) findViewById(R.id.tvKeepMeSigin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        mEdtpass = (EditText) findViewById(R.id.etPassword);

        setSupportActionBar(toolbar);
        gson = new Gson();

        this.mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
//        Bundle bundle = getIntent().getExtras();
//        regID = bundle.getString("regID");
        regID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
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

        if (getAutoLogIn()) {
            String email = MySharePreferences.getValue(this, "email", "");
            String token = MySharePreferences.getValue(this, "token", "");
//            String pass = MySharePreferences.getValue(this, "password", "");
            mEdtEmail.setText(email);
//            mEdtpass.setText(token);

            if (email.isEmpty() || token.isEmpty()) {
                return;
            } else {
                Log.d("Test-------------", token + "");
                autoLogIn(email, token);
            }
        }

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEdtpass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

                final String email = mEdtEmail.getText().toString();
                final String password = mEdtpass.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Can not be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginID_isLegal && loginPW_isLegal) {
//                    mUser = mDatabaseHandler.getUser(email);
//                    if (email.equals(mUser.getEmail()) && password.equals(mUser.getPassWord())) {
                    mDialog.show();
                    RequestUtils.logIn(email, password, regID, new RequestUtils.logInCallback() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("STATUS CODE_Fail", statusCode + "" + responseString);
                            mDialog.dismiss();
                            showError(responseString);
//                            if (statusCode == 400) {
//
//                                Toast.makeText(getApplicationContext(), "LogIn Fail,token is wrong account was logged by other devices", Toast.LENGTH_SHORT).show();
//                            } else if (statusCode == 500) {
//                                Toast.makeText(getApplicationContext(), "LogIn Fail, please check network", Toast.LENGTH_SHORT).show();
//                            } else if (statusCode == 404) {
//                                Toast.makeText(getApplicationContext(), "Email or Password is wrong", Toast.LENGTH_SHORT).show();
//                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.d("STATUS CODE", statusCode + "" + responseString);
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            mDialog.dismiss();
                            ResponseString st = gson.fromJson(responseString, ResponseString.class);
                            String token = st.getToken();
                            MySharePreferences.setValue(getApplicationContext(), "email", email);
//                            if (getAutoLogIn()) {
                            MySharePreferences.setValue(getApplicationContext(), "token", token);

//                            }

//                            MySharePreferences.setValue(getApplicationContext(), "password", password);
                            Log.d("TOKEN LOGIN", token + "");

                            finish();
                        }
                    });


                } else {
                    if (!loginID_isLegal) {
                        if (mEdtEmail.length() > USERNAME_MAX) {
                            mEdtEmail.setError(">" + USERNAME_MAX);
                        } else if (mEdtEmail.length() < USERNAME_MIN) {
                            mEdtEmail.setError("<" + USERNAME_MIN);
                        } else {
                            mEdtEmail.setError("Email is wrong format");
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

        cbKeepMeSigin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAutoLogIn(b);
            }
        });


    }

    private void showError(String responseString) {
        Error error = gson.fromJson(responseString, Error.class);
        String totalError = "";
        for (String e : error.getData()) {
            totalError += e + "\n";
        }
        Toast.makeText(getApplicationContext(), totalError, Toast.LENGTH_LONG).show();
    }

    private void autoLogIn(String email, String token) {
        if ((email != null) && (token != null)) {
            final ProgressDialog mDialog = new ProgressDialog(LogInActivity.this);
            mDialog.setTitle("Loging....");
            mDialog.show();
            RequestUtils.autoLogIn(email, token, regID, new RequestUtils.logInCallback() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("LogIn Fail", statusCode + "---" + responseString);
                    mDialog.dismiss();
                    showError(responseString);
//                    if (statusCode == 400) {
//                        Toast.makeText(getApplicationContext(), "LogIn Fail,token is wrong account was logged by other devices", Toast.LENGTH_SHORT).show();
//                    } else if (statusCode == 500) {
//                        Toast.makeText(getApplicationContext(), "LogIn Fail, please check network", Toast.LENGTH_SHORT).show();
//                    } else if (statusCode == 404) {
//                        Toast.makeText(getApplicationContext(), "Email or Password is wrong", Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("LogIn Success", statusCode + "---" + responseString);
//                    statusCode-- - trạng thái sever trả về
                    // statusCode = 200 ---  thành công
                    // statusCode = 400 ---  bad request (sai mật khẩu)
                    // statusCode = 500 ---  không thành công (do sever)
                    // statusCode = 404 ---  không thành công (sai tài khoản)

                    // responseString --- giá trị server trả về (chuỗi json)
                    Log.d("STATUS CODE", statusCode + "");
                    Log.d("RESPONSE STRING", responseString + "");
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    mDialog.dismiss();
                    finish();
                }
            });


        }

    }

    private void saveAutoLogIn(boolean checked) {
        MySharePreferences.setValue(getApplicationContext(), "checked", checked);
    }

    private Boolean getAutoLogIn() {
        return MySharePreferences.getValue(getApplicationContext(), "checked", false);
    }

    public class Error {
        private List<String> data;

        @Override
        public String toString() {
            return "Error{" +
                    "data=" + data +
                    '}';
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }
}
