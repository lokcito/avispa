package info.rayrojas.avispa.services;

import android.util.Log;

import info.rayrojas.avispa.conf.Settings;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.ConnectionStateListener;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;

import static io.ably.lib.realtime.ConnectionEvent.connected;

public class AblyService {
    SpyService spy;
    public AblyService(SpyService _spy) {
        this.spy = _spy;
    }
    public void listen() {
        try {
            AblyRealtime ably = new AblyRealtime(Settings.CLIENT_TOKEN);
            ably.connection.on(new ConnectionStateListener() {
                @Override
                public void onConnectionStateChanged(ConnectionStateListener.ConnectionStateChange state) {
                    switch (state.current) {
                        case connected: {
                            // Successful connection
                            break;
                        }
                        case failed: {
                            // Failed connection
                            break;
                        }
                    }
                }
            });
            Channel channel = ably.channels.get(Settings.CLIENT_CHANNEL_INFO);
            String[] events = new String[] {"ok", "event2"};
            channel.subscribe(events, new Channel.MessageListener() {
                @Override
                public void onMessage(Message message) {
                    AblyService.this.spy.pullData("Ably", Settings.CLIENT_CHANNEL_INFO,
                            "any", message.data.toString());
//                    Log.v("bichito", "Received `" +messages.data + "` message with data");
                }
            });


        } catch (AblyException e) {
            e.printStackTrace();
        }

    }

}
