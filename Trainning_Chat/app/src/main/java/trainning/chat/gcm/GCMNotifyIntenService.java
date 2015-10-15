package trainning.chat.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import trainning.chat.R;
import trainning.chat.activity.ChatActivity;

/**
 * Created by ASUS on 15/10/2015.
 */
public class GCMNotifyIntenService extends IntentService {


    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String messReceive;

    public GCMNotifyIntenService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!bundle.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send Error" + bundle.toString());
                // Check Message Delete
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + bundle.toString());
                // Message receive
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                messReceive = (String) bundle.get(GCMConfig.MESSAGE_KEY);
                sendNotification("pre-message: " + messReceive);
                Log.i(GCMConfig.LOG_TAG, "Received: " + bundle.toString());
            }
        }
    }

    public void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ChatActivity.class), 0);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Notification test")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).setContentText(msg);
        // Set penddingIntent
        mBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
