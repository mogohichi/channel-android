package co.getchannel.channel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import co.getchannel.channel.helpers.CHConstants;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHConfiguration  {
    public static String _applicationId;
    public final static String getApplicationId() {
        try {
         if(_applicationId != null)
             if( _applicationId.length() > 0)
                 return  _applicationId;

            SharedPreferences sharedPref = Channel.getSharedPreferences();
            _applicationId  = sharedPref.getString("applicationId","");
            return _applicationId;
        }catch (Exception e) {
            Log.d(CHConstants.kChannel_tag, e.getMessage());
            return "";
        }
    }

    public final static void setApplicationId( String applicationId) {
        try {
            _applicationId = applicationId;
            SharedPreferences sharedPref = Channel.getSharedPreferences();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("applicationId", applicationId);
            editor.commit();
        }catch (Exception e) {
            Log.d(CHConstants.kChannel_tag, e.getMessage());
        }
    }
}