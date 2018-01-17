package co.getchannel.example;

/**
 * Created by rataphon on 9/23/2017 AD.
 */


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import co.getchannel.channel.Channel;
import co.getchannel.channel.callback.ChannelCallback;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
//        Channel.saveDeviceToken(token, new ChannelCallback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFail(String message) {
//
//            }
//        });
    }
}