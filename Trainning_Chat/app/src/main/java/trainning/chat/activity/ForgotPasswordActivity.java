package trainning.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import trainning.chat.R;

/**
 * Created by ASUS on 10/10/2015.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText mEdtEmail;
    private Button mBtnForgotPassword;
    private Toolbar mToolbar;
    public final int USERNAME_MAX = 256;
    public final int USERNAME_MIN = 3;
    public Pattern emailLegalPattern = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    private boolean loginID_isLegal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        mEdtEmail = (EditText) findViewById(R.id.etEmail);
        mBtnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Forgot Your Pasword");
        setSupportActionBar(mToolbar);
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
        mBtnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loginID_isLegal) {
                    if (mEdtEmail.length() > USERNAME_MAX) {
                        mEdtEmail.setError(">" + USERNAME_MAX);
                    } else if (mEdtEmail.length() < USERNAME_MIN) {
                        mEdtEmail.setError("<" + USERNAME_MIN);

                    } else {
                        mEdtEmail.setError("Email not match");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Forgot Password Success", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
