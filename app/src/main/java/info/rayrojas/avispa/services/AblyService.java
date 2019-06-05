package info.rayrojas.avispa.services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import info.rayrojas.avispa.conf.Settings;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.ConnectionStateListener;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;

import static io.ably.lib.realtime.ConnectionEvent.connected;

public class AblyService {
    SpyService spy;
    AblyRealtime ably;
    public AblyService(SpyService _spy) {
        this.spy = _spy;
    }
    public void close (){
        ably.close();
    }
    public void listen() {
        try {
            ably = new AblyRealtime(Settings.CLIENT_TOKEN);
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
                    Log.v("bichito", "Received message with data" + message.id + "--->" + message.clientId + "<<");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(message.data.toString());
                    }catch (JSONException err){
                        Log.d("Error", err.toString());
                        return;
                    }
                    String event = "any";

                    if ( jsonObject != null ) {
                        try {
                            JSONObject extra = jsonObject.getJSONObject("extra");
                            if ( extra != null ) {
                                event = extra.getString("event");
                            }
                        } catch (JSONException e) {

                        }
                    }
//

                    AblyService.this.spy.pullData("Ably", Settings.CLIENT_CHANNEL_INFO,
                            event, message.data.toString());

                }
            });


        } catch (AblyException e) {
            e.printStackTrace();
        }

    }

}
