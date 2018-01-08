package co.getchannel.example;

/**
 * Created by rataphon on 9/23/2017 AD.
 */

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import co.getchannel.channel.Channel;
import co.getchannel.channel.callback.ChannelCallback;
import co.getchannel.channel.models.CHClient;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Channel.postbackPushNotification(remoteMessage.getData(), new ChannelCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String message) {

            }
        });

    }
}