package trainning.chat.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import trainning.chat.R;
import trainning.chat.activity.ChatActivity;
import trainning.chat.entity.chatroom.DataChat;
import trainning.chat.entity.chatroom.MessageChat;

/**
 * Created by ASUS on 06/08/2015.
 */
public class GCMNotificationIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    public static final String TAG = "GCM NotificationIntentService";

    public GCMNotificationIntentService(String name) {
        super(name);
    }

    public GCMNotificationIntentService() {
        super("GCMNotificationIntentService");
    }

    public String messReceive;

    @Override
    protected void onHandleIntent(Intent intent) {
        //get data bundle
        Bundle extras = intent.getExtras();
        //initialization gcm instance
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // Get message type from GCM send
        String messageType = gcm.getMessageType(intent);


        if (!extras.isEmpty()) {

            // Check Message Error
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send Error" + extras.toString());
                // Check Message Delete
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // Message receive
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                messReceive = (String) extras.get("data");
                Gson gson = new Gson();
                MessageChat content = gson.fromJson(intent.getStringExtra("data"), MessageChat.class);
                String data = content.getData();
                if (!ChatActivity.isrunning) {
                    sendNotification("" + data);
                }

                Log.i(GCMConfig.LOG_TAG, "Received: " + extras.toString());
            }

        }

    }

    private void sendNotification(String msg) {

//        Bundle args = new Bundle();
//        args.putString("stringmessage", msg);
//        Intent chat = new Intent(this, ChatActivity.class);
//        chat.putExtra("message", args);


        Log.d(GCMConfig.LOG_TAG, "Preparing to send notification..." + msg);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create PendingIntent
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ChatActivity.class), 0);
//        // Setup Notify
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_mail_new)
                .setContentTitle("Tin nhắn mới")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).setContentText(msg);
//        // Set penddingIntent
//        mBuilder.setContentIntent(contentIntent);
        Intent resultIntent = new Intent(this, ChatActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChatActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(GCMConfig.LOG_TAG, "Notification sent successfully.");
    }

}
