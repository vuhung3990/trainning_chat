package trainning.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import trainning.chat.gcm.GCMRegister;
import trainning.chat.R;

/**
 * Created by ASUS on 13/10/2015.
 */
public class SplashActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mContext = this;
        GCMRegister register = new GCMRegister(this, new GCMRegister.registerListener() {
            @Override
            public void onSuccess(String regID) {
                Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                intent.putExtra("regID", regID);
                startActivity(intent);
                finish();
//                startActivity(new Intent(SplashActivity.this, LogInActivity.class));
            }

            @Override
            public void onFail() {
                Toast.makeText(mContext, "please check your network connection and try again later.", Toast.LENGTH_LONG).show();
            }
        });
        register.execute();
    }
}
