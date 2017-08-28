package co.getchannel.channel;

/**
 * Created by Admin on 8/16/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


import co.getchannel.channel.activities.ChatActivity;
import co.getchannel.channel.models.CHClient;

import static co.getchannel.channel.helpers.CHConstants.kChannel_tag;

public class Channel  {
    private static Activity activity;
    private static Channel channelInstance = null; // the only instance of the class
    public static Channel getInstance()
    {
        if (channelInstance == null)
        {
            channelInstance = new Channel();
        }
        return channelInstance;
    }
    public final static void test(String applicationId){
        Log.d(kChannel_tag,CHConfiguration.getApplicationId());
    }

    public static Activity getActivity() {
        return activity;
    }

    public final static void setupActivityWithApplicationID(final WeakReference<Activity> mReference,String applicationID){
      Activity act = mReference.get();
        if (act != null) {
            activity = act;
            setupWithApplicationID(applicationID,null,null);
       }
        Log.d(kChannel_tag,CHConfiguration.getApplicationId());
    }

    public final static void setupActivityWithApplicationID(final WeakReference<Activity> mReference,String applicationID,String userID,HashMap<String,String>userData){
        Activity act = mReference.get();
        if (act != null) {
            activity = act;
           setupWithApplicationID(applicationID,userID,userData);
        }
        Log.d(kChannel_tag,CHConfiguration.getApplicationId());
    }
    public final static void setupWithApplicationID(String applicationId,String userID,HashMap<String,String>userData){
        CHConfiguration.setApplicationId(applicationId);
        if (userID != null){
            CHClient.currentClient().setUserID(userID);

        }
        if(userData != null){
            CHClient.currentClient().setUserData(userData);
        }

        CHClient.connectClientWithUserID(userID,userData);
    }


    public final static void chatViewWithUserID(String userID, HashMap<String,String> userData){
        Intent myIntent = new Intent(activity, ChatActivity.class);
        myIntent.putExtra("userData",userData);
        myIntent.putExtra("userID",userID);
        activity.startActivity(myIntent);
    }

}
