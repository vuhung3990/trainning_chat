package trainning.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ASUS on 09/10/2015.
 */
public class RegisterActivity extends Activity {
    private EditText edtUsername, edtEmail, edtPassword, edtConfirmPass;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        edtUsername = (EditText) findViewById(R.id.etUserName);
        edtEmail = (EditText) findViewById(R.id.etEmail);
        edtConfirmPass = (EditText) findViewById(R.id.etConfirmPassword);
        edtPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnReg);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    if (password.equals(confirmPass)) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.addUser(new User(userName, email, password));
                        startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "password not match ", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Email or password must not empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
