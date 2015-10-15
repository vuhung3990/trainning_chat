package trainning.chat.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import de.greenrobot.event.EventBus;

/**
 * Created by ASUS on 15/10/2015.
 */
public class GCMBroadCastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName=new ComponentName(context.getPackageName(),GCMNotifyIntenService.class.getName());
        EventBus.getDefault().post(intent.getStringExtra(GCMConfig.MESSAGE_KEY));
        startWakefulService(context, intent.setComponent(componentName));
        setResultCode(Activity.RESULT_OK);
    }
}
