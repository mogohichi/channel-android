package co.getchannel.example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import co.getchannel.channel.Channel;
import co.getchannel.channel.callback.ChannelCallback;
import co.getchannel.channel.models.CHClient;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity thisActivity = this;
        setContentView(R.layout.activity_main);

        Channel.setupApplicationContextWithApplicationKey(this.getApplicationContext(), "app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw", new ChannelCallback() {
            @Override
            public void onSuccess() {
                final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Channel.saveDeviceToken(refreshedToken, new ChannelCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Example","setupApplicationContextWithApplicationKey success");
                    }

                    @Override
                    public void onFail(String message) {
                        Log.d("Example","setupApplicationContextWithApplicationKey fail");
                    }
                });
            }

            @Override
            public void onFail(String message) {
                Log.d("Example","setupApplicationContextWithApplicationKey fail");
            }
        });

//        String userID = "Tui_Test_SystemName4";
//        HashMap<String,String> userData =  new HashMap<String,String>();
//        userData.put("name","Tui");
//        userData.put("lastname","Bombadua");
//        Channel.setupApplicationContextWithApplicationKeyAndUser(this.getApplicationContext(), "app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw", userID, userData, new ChannelCallback() {
//            @Override
//            public void onSuccess() {
//                final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//                Channel.saveDeviceToken(refreshedToken, new ChannelCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("Example","notifyButton success");
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Log.d("Example","notifyButton fail");
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(String message) {
//
//            }
//        });


        Button clickButton = (Button) findViewById(R.id.contactButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String userID = "Tui_Test_SystemName5";
                HashMap<String,String> userData =  new HashMap<String,String>();
                userData.put("name","John");
                userData.put("lastname","Snow");
                Channel.chatViewWithUserID(thisActivity, userID, userData);
//               Channel.chatView(thisActivity);

            }
        });

        Button notifyButton = (Button) findViewById(R.id.notifyButton);
        notifyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.showLatestNotification(thisActivity);
//                Channel.showLatestNotification(thisActivity, new ChannelCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("Example","notifyButton success");
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Log.d("Example","notifyButton fail");
//                    }
//                });
            }
        });


        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.subscribeToTopic("tui");
//                Channel.subscribeToTopic("tui", new ChannelCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("Example","subscribeButton success");
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Log.d("Example","subscribeButton fail");
//                    }
//                });
            }
        });


        Button unsubscribeButton = (Button) findViewById(R.id.unsubscribeButton);
        unsubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.unsubscribeFromTopic("tui");
//                Channel.unsubscribeFromTopic("tui", new ChannelCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("Example","unsubscribeButton success");
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Log.d("Example","unsubscribeButton fail");
//                    }
//                });
            }
        });
    }
}
