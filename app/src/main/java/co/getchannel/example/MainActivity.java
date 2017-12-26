package co.getchannel.example;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import co.getchannel.channel.Channel;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeakReference<Activity> weakReference = new WeakReference<Activity>((Activity)this);

        String userID = "Tui_Test_SystemName4";
        HashMap<String,String> userData =  new HashMap<String,String>();
        userData.put("name","Tui");
        userData.put("lastname","Bombadua");

       // Channel.setupActivityWithApplicationKey(weakReference,"app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw",userID,userData);
        Channel.setupActivityWithApplicationKey(weakReference,"app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw");

        Button clickButton = (Button) findViewById(R.id.contactButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                String userID = "AppID";
//                HashMap<String,String> userData =  new HashMap<String,String>();
//                userData.put("name","John");
//                userData.put("lastname","Snow");
//                Channel.chatViewWithUserID( userID, userData);
                Channel.chatView();

            }
        });

        Button btn = (Button) findViewById(R.id.notifyButton);
        final Activity thisActivity = this;
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.unsubscribeFromTopic("tui");
//                Channel.showLatestNotification(thisActivity);
            }
        });

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Channel.saveDeviceToken(token);
//        Log.d("firebase", "Token : " + token);

    }
}
