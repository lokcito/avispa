package info.rayrojas.avispa.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import info.rayrojas.avispa.conf.Settings;
import info.rayrojas.avispa.models.Notify;

public class PusherService {
    SpyService spy;
    public PusherService(SpyService _spy) {
        spy = _spy;
    }

    public void listen() {
        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher(Settings.CLIENT_TOKEN, options);

        Channel channel = pusher.subscribe(Settings.CLIENT_CHANNEL_INFO);

        channel.bind(Settings.CLIENT_EVENT_INFO, new SubscriptionEventListener() {
            @Override
            public void onEvent(final String channelName, final String eventName, final String data) {
                PusherService.this.spy.pullData("Pusher", channelName,
                        "any", data);
//                Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
            }
        });

        pusher.connect();
    }
}
