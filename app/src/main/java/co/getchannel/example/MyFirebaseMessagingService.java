package co.getchannel.example;

/**
 * Created by rataphon on 9/23/2017 AD.
 */

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import co.getchannel.channel.models.CHClient;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //CHClient.currentClient().postbackPushNotification(remoteMessage.getData());
    }
}