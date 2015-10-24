package trainning.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import trainning.chat.gcm.GCMRegister;
import trainning.chat.R;
import trainning.chat.util.MySharePreferences;

/**
 * Created by ASUS on 13/10/2015.
 */
public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    private boolean checkautoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        checkautoLogin = MySharePreferences.getValue(this, "checked", false);
        mContext = this;
        GCMRegister register = new GCMRegister(this, new GCMRegister.registerListener() {
            @Override
            public void onSuccess(String regID) {
                if (isNetworkAvailable()) {
                    if (checkautoLogin == true) {

                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.putExtra("regID", regID);
                        startActivity(intent);
                        finish();
//                startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                        intent.putExtra("regID", regID);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(mContext, "Not connected please check your network connection and try again later.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFail() {
                Toast.makeText(mContext, "Not connected please check your network connection and try again later.", Toast.LENGTH_LONG).show();
            }
        }

        );
        register.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
