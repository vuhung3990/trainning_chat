package trainning.chat.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GCMRegister extends AsyncTask<Void, Void, String> {
    private final Context mContext;
    private SharedPreferences mSharedPreferences;
    private final registerListener callback;
    private String regID;


    public GCMRegister(Context context, registerListener callback) {
        this.mContext = context;
        this.callback = callback;
        this.mSharedPreferences = context.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        regID = mSharedPreferences.getString(GCMConfig.PREFERENCE_KEY_REG_ID, null);
    }

    @Override
    protected String doInBackground(Void... params) {
        if (regID == null) {
            try {
                // get instance gcm by context
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
                // return regID with google project number
                return gcm.register(GCMConfig.GOOGLE_PROJECT_NUMBER);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } else {

            return regID;

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (!TextUtils.isEmpty(s)) {
            mSharedPreferences.edit().putString(GCMConfig.PREFERENCE_KEY_REG_ID, s).commit();
            regID = s;
            Log.d(GCMConfig.LOG_TAG, "SUCCESS" + regID);

            callback.onSuccess(regID);
        } else {
            Log.d(GCMConfig.LOG_TAG, "ERROR register ID null");
            callback.onFail();
        }
    }

    public interface registerListener {
        void onSuccess(String regID);

        void onFail();
    }
}

