package trainning.chat.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import trainning.chat.entity.DataChat;

/**
 * Created by ASUS on 06/08/2015.
 */
public class GCMBroadcastReciever extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create new component identifier
//        Bundle extras = intent.getExtras();
//        Intent msgrcv = new Intent("Msg");
//        msgrcv.putExtra("message", extras.getString("stringmessage"));


//        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        ComponentName componentName = new ComponentName(context.getPackageName(), GCMNotificationIntentService.class.getName());


//        Log.d("DSDSD", intent.getStringExtra("data"));
        if (intent.getStringExtra("data") != null) {
            Gson gson = new Gson();
            DataChat content = gson.fromJson(intent.getStringExtra("data"), DataChat.class);
            String data = content.getData();
            Log.d("DATA", data);

            EventBus.getDefault().post(data);
        }

        startWakefulService(context, intent.setComponent(componentName));
        setResultCode(Activity.RESULT_OK);
    }
}
