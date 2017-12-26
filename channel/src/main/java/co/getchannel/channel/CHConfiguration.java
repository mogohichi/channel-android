package co.getchannel.channel;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHConfiguration  {
    public final static String getApplicationId() {
        String applicationId = "";
        SharedPreferences sharedPref = Channel.getSharedPreferences();
        applicationId = sharedPref.getString("applicationId","");
        return applicationId;
    }

    public final static void setApplicationId( String applicationId) {
            SharedPreferences sharedPref = Channel.getSharedPreferences();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("applicationId", applicationId);
            editor.commit();
    }
}