package info.rayrojas.avispa.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import info.rayrojas.avispa.MainActivity;

public class SpyService extends Service {
    private static final int NOTIFICATION_ID = 1;
    public int counter = 0;
    Context context;
    public SpyService() {

    }
    public SpyService(Context applicationContext) {
        super();
        context = applicationContext;
        Log.v("bichito", "aqui sse crea el servicio");
    }

    private void showForegroundNotification(String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("xxcxc")
                .setContentText(contentText)
//                .setSmallIcon(R.drawable.ic_notification)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher("59478312f8e6af560ebc", options);

        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(final String channelName, final String eventName, final String data) {
                //editText.setText(data);
//                showForegroundNotification(data);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SpyService.this, data, Toast.LENGTH_SHORT).show();
                    }
                });

//                Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
            }
        });

        pusher.connect();
        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("bichito", "aqui sse muere el servicio");
//        Intent broadcastIntent = new Intent("ac.in.ActivityRecognition.RestartSensor");
//        sendBroadcast(broadcastIntent);
    }
}
