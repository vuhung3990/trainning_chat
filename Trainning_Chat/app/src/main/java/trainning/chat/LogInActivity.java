package trainning.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ASUS on 09/10/2015.
 */
public class LogInActivity extends Activity {
    private EditText mEdtEmail, mEdtpass;
    private Button mBtnLogIn;
    private User mUser;
    private DatabaseHandler mDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mEdtEmail = (EditText) findViewById(R.id.etEmail);
        mEdtpass = (EditText) findViewById(R.id.etPassword);
        mBtnLogIn = (Button) findViewById(R.id.btnLogIn);
        mBtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEdtEmail.getText().toString();
                String password = mEdtpass.getText().toString();
                mDatabaseHandler = new DatabaseHandler(getApplicationContext());
                mUser = mDatabaseHandler.getUser(email);
                if (!email.equals("") && !password.equals("")) {
                    if (email.equals(mUser.getEmail()) && password.equals(mUser.getPassWord())) {
                        Toast.makeText(getApplicationContext(), "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Email or Password not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Email or password is not empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
