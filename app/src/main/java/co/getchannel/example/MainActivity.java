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
//        Channel.setupActivityWithApplicationID(weakReference,"app_dK5cqQHzHiSYE6DfW7SfCUDemfj444LmEmow0OkpyUw");


        String userID = "Tui_Test_SystemName3";
        HashMap<String,String> userData =  new HashMap<String,String>();
        userData.put("name","Tui");
        userData.put("lastname","Bombadua");
//       Channel.setupActivityWithApplicationID(weakReference,"app_I8ggyuX6pnx3mlhjzRCMMClRkWwvhD2XKodKvq0tx2U",userID,userData);
        Channel.setupActivityWithApplicationKey(weakReference,"app_8mMKW3tfvTd3QLdKWznFS63r1qHj-nd6Z6nmb7ySBWw",userID,userData);

        Button clickButton = (Button) findViewById(R.id.contactButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String userID = "AppID";
                HashMap<String,String> userData =  new HashMap<String,String>();
                userData.put("name","John");
                userData.put("lastname","Snow");
                Channel.chatViewWithUserID( userID, userData);

            }
        });

        Button btn = (Button) findViewById(R.id.notifyButton);
        final Activity thisActivity = this;
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel.showLatestNotification(thisActivity);

            }
        });

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Channel.saveDeviceToken(token);
//        Log.d("firebase", "Token : " + token);

    }
}
