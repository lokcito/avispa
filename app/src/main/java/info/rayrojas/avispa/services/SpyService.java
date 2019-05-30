package info.rayrojas.avispa.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import info.rayrojas.avispa.MainActivity;
import info.rayrojas.avispa.R;
import info.rayrojas.avispa.conf.Settings;
import info.rayrojas.avispa.models.Notify;
import info.rayrojas.avispa.notifiers.NotificationView;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class SpyService extends Service {
    private static final int NOTIFICATION_ID = 1;
    public int counter = 0;
    Context context;
    private CallBack mCallBack;
    private MyBinder mLocalbinder = new MyBinder();
    public SpyService() {

    }
    public interface CallBack {
        void onOperationProgress(int progress);
        void onOperationCompleted();
    }
    public class MyBinder extends Binder {
        public SpyService getService() {
            return SpyService.this;
        }
    }
    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
    public SpyService(Context applicationContext) {
        super();
        context = applicationContext;
        Log.v("bichito", "aqui sse crea el servicio");
    }

    public void Notification(String title, String message) {
        // Set Notification Title
        String strtitle = "sss";
        // Set Notification Text
        String strtext = "sds";

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, NotificationView.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_menu_manage)
                // Set Ticker Message
                .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle(title)
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.drawable.ic_menu_camera, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

    private void showForegroundNotification(String contentText) {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://www.wgn.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setCategory(Notification.CATEGORY_PROMO)
                .setContentTitle("aaaa")
                .setContentText("ssss")
                .setAutoCancel(true)
                .setVisibility(VISIBILITY_PUBLIC)
                .addAction(android.R.drawable.ic_menu_view, "View details", contentIntent)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalbinder;
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher(Settings.PUSHER_TOKEN, options);

        Channel channel = pusher.subscribe(Settings.PUSHER_CHANNEL_INFO);

        channel.bind(Settings.PUSHER_EVENT_INFO, new SubscriptionEventListener() {
            @Override
            public void onEvent(final String channelName, final String eventName, final String data) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data);
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                    return;
                }
                if ( jsonObject == null ) {
                    return;
                }
                String _title = "Pusher Notification";
                String _message = data;
                String _extra = "-";
                try {
                    _title = jsonObject.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    _message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    _extra = jsonObject.getString("_extra");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String title = _title;
                final String message = _message;
                final String extra = _extra;
                //editText.setText(data);
//                showForegroundNotification(data);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                    Notify currentUser = new Notify();
                    currentUser.setId(0);
                    currentUser.setTitle(title);
                    currentUser.setMessage(message);
                    currentUser.setExtra(extra);
                    currentUser.setToken("0");
                    currentUser.setLocal(SpyService.this);
                    Notification(title, message);
                    if (mCallBack != null) {
                        mCallBack.onOperationCompleted();
                    }

//                        showForegroundNotification("xxxxg");
//                        Toast.makeText(SpyService.this, data, Toast.LENGTH_SHORT).show();
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
