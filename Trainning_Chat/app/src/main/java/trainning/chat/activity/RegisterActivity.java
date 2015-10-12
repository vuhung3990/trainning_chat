package trainning.chat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * Created by ASUS on 09/10/2015.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername, edtEmail, edtPassword, edtConfirmPass;
    private Button btnRegister;
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
        setContentView(R.layout.register_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);
        edtUsername = (EditText) findViewById(R.id.etUserName);
        edtEmail = (EditText) findViewById(R.id.etEmail);
        edtConfirmPass = (EditText) findViewById(R.id.etConfirmPassword);
        edtPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnReg);


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
        edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                edtPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPassword.length() < PASSWORD_MIN) {
                    // mLoginPassword.setError("< "+PASSWORD_MIN);
                    loginPW_isLegal = false;
                } else {
                    // mLoginPassword.setError(null);
                    loginPW_isLegal = true;
                }
            }

        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                                               mDialog.setTitle("Creating account");
                                               mDialog.setMessage("Please wait...");

                                               String userName = edtUsername.getText().toString();
                                               String email = edtEmail.getText().toString();
                                               String password = edtPassword.getText().toString();
                                               String confirmPass = edtConfirmPass.getText().toString();
                                               if (email.isEmpty() || password.isEmpty()) {
                                                   Toast.makeText(getApplicationContext(), "Can not be blank", Toast.LENGTH_SHORT).show();
                                                   return;

                                               }
                                               if (loginID_isLegal && loginPW_isLegal) {
                                                   if (password.equals(confirmPass)) {
//                                                       DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//                                                       db.addUser(new User(userName, email, password));
                                                       mDialog.show();
//                                                       startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                                                       AsyncHttpClient client = new AsyncHttpClient();
                                                       RequestParams params = new RequestParams();
                                                       params.put("email", email);
                                                       params.put("name", userName);
                                                       params.put("password", password);
                                                       client.post("http://trainningchat-vuhung3990.rhcloud.com/user", params, new TextHttpResponseHandler() {
                                                           @Override
                                                           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                                                           }

                                                           @Override
                                                           public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                               mDialog.dismiss();
                                                               Toast.makeText(getApplicationContext(), "register success", Toast.LENGTH_SHORT).show();


                                                           }
                                                       });


                                                   } else {
                                                       Toast.makeText(getApplicationContext(), "password not match ", Toast.LENGTH_SHORT).show();

                                                   }

                                               } else

                                               {
                                                   if (!loginID_isLegal) {
                                                       if (edtEmail.length() > USERNAME_MAX) {
                                                           edtEmail.setError(">" + USERNAME_MAX);
                                                       } else if (edtEmail.length() < USERNAME_MIN) {
                                                           edtEmail.setError("<" + USERNAME_MIN);

                                                       } else {
                                                           edtEmail.setError("Email not match");
                                                       }
                                                   }
                                                   if (!loginPW_isLegal) {
                                                       if (edtPassword.length() < PASSWORD_MIN) {
                                                           edtPassword.setError("<" + PASSWORD_MIN);
                                                       }
                                                   }
                                               }


                                           }
                                       }

        );


    }
}
