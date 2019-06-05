package info.rayrojas.avispa.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Message;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import info.rayrojas.avispa.conf.Settings;

public class ScaleDroneService {
    SpyService spy;
    public ScaleDroneService(SpyService _spy) {
        spy = _spy;
    }
    public void listen() {

        final Scaledrone drone = new Scaledrone(Settings.CLIENT_CHANNEL_INFO);
        drone.subscribe(Settings.CLIENT_EVENT_INFO, new RoomListener() {
            @Override
            public void onOpen(Room room) {

            }

            @Override
            public void onOpenFailure(Room room, Exception ex) {

            }

            @Override
            public void onMessage(Room room, Message message) {
                ScaleDroneService.this.spy.pullData("ScaleDrone",
                        Settings.CLIENT_CHANNEL_INFO, Settings.CLIENT_EVENT_INFO,
                        message.toString());
                // parse as string
//                System.out.println("Message: " + message.getData().asText());

                // or parse as POJO
//                try {
//                    ObjectMapper mapper = new ObjectMapper();
//                    System.out.println("Message: " + message.getData());
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
            }
            // overwrite other methods
        });

    }
}
