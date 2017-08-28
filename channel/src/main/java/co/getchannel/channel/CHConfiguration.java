package co.getchannel.channel;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHConfiguration  {
    public final static String getApplicationId() {
        String applicationId = "";
        Activity act = Channel.getActivity();
        if (act != null) {
            SharedPreferences sharedPref = act.getPreferences(android.content.Context.MODE_PRIVATE);
            applicationId = sharedPref.getString("applicationId","");
        }
        return applicationId;
    }

    public final static void setApplicationId( String applicationId) {
        Activity act = Channel.getActivity();
        if (act != null) {
            SharedPreferences sharedPref = act.getPreferences(android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("applicationId", applicationId);
            editor.commit();
        }
    }
}