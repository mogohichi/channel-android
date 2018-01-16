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
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Channel.saveDeviceToken(refreshedToken, new ChannelCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
            }

            @Override
            public void onFail(String message) {

            }
        });
//        String userID = "Tui_Test_SystemName4";
//        HashMap<String,String> userData =  new HashMap<String,String>();
//        userData.put("name","Tui");
//        userData.put("lastname","Bombadua");
//                Channel.setupApplicationContextWithApplicationKey(this.getSharedPreferences("Channel", android.content.Context.MODE_PRIVATE)
//              ,"app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw",userID,userData);


        Button clickButton = (Button) findViewById(R.id.contactButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                String userID = "AppID";
//                HashMap<String,String> userData =  new HashMap<String,String>();
//                userData.put("name","John");
//                userData.put("lastname","Snow");
//                Channel.chatViewWithUserID(thisActivity, userID, userData);
                Channel.chatView(thisActivity);

            }
        });

        Button btn = (Button) findViewById(R.id.notifyButton);
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.subscribeToTopic("tui", new ChannelCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
                Channel.unsubscribeFromTopic("tui", new ChannelCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
                Channel.showLatestNotification(thisActivity, new ChannelCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
            }
        });



    }
}
