package info.rayrojas.avispa.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FbMessagingService extends FirebaseMessagingService {
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
    }
}
