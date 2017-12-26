package co.getchannel.channel;

/**
 * Created by Admin on 8/16/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;
import co.getchannel.channel.activities.ChatActivity;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.callback.ChannelProcessComplete;


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

    private static SharedPreferences sharedPreferences;
    private static String packageName;

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        Channel.sharedPreferences = sharedPreferences;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static void setPackageName(String packageName) {
        Channel.packageName = packageName;
    }

    public final static void setupApplicationContextWithApplicationKey(Context context, String applicationID){
        setPackageName(context.getPackageName());
        setSharedPreferences(context.getSharedPreferences("Channel", android.content.Context.MODE_PRIVATE));
        setupApplicationAndClient(applicationID,null,null);
    }

    public final static void setupApplicationContextWithApplicationKeyAndUser(Context context,String applicationID,String userID,HashMap<String,String>userData){
        setPackageName(context.getPackageName());
        setSharedPreferences(context.getSharedPreferences("Channel", android.content.Context.MODE_PRIVATE));
        setupApplicationAndClient(applicationID,userID,userData);
    }

   private static void setupApplicationAndClient(String applicationId,String userID,HashMap<String,String>userData){
        CHConfiguration.setApplicationId(applicationId);
        CHClient.currentClient().setUserID(userID);
        CHClient.currentClient().setUserData(userData);
        CHClient.connectClientWithUserID(userID, userData, new ChannelProcessComplete() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    public final static void chatView(final Activity activity){
        Intent myIntent = new Intent(activity, ChatActivity.class);
        activity.startActivity(myIntent);
    }

    public final static void chatViewWithUserID(final Activity activity,String userID, HashMap<String,String> userData){
        Intent myIntent = new Intent(activity, ChatActivity.class);
        myIntent.putExtra("userData",userData);
        myIntent.putExtra("userID",userID);
        activity.startActivity(myIntent);
    }

    public final static void showLatestNotification(final Activity activity){
        if (CHClient.currentClient().getClientID().length() > 0) {
            CHClient.currentClient().checkNewNotification(activity, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(String message) {

                }
            });
        }else{
            CHClient.connectClientWithUserID(null, null, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    CHClient.currentClient().checkNewNotification(activity, new ChannelProcessComplete() {
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
        }
    }

    public final static void saveDeviceToken(String token){
        final String thisToken = token;
        if (CHClient.currentClient().getClientID().length() > 0) {
            CHClient.currentClient().saveDeviceToken(token, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(String message) {

                }
            });
        }else{
            CHClient.connectClientWithUserID(null, null, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    CHClient.currentClient().saveDeviceToken(thisToken, new ChannelProcessComplete() {
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
        }
    }

    public final static void subscribeToTopic(String topic){
        final String thisTopic = topic;
        if (CHClient.currentClient().getClientID().length() > 0) {
            CHClient.currentClient().subscribeToTopic(thisTopic, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(String message) {

                }
            });
        }else{
            CHClient.connectClientWithUserID(null, null, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    CHClient.currentClient().subscribeToTopic(thisTopic, new ChannelProcessComplete() {
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
        }
    }

    public final static void unsubscribeFromTopic(String topic){
        final String thisTopic = topic;
        if (CHClient.currentClient().getClientID().length() > 0) {
            CHClient.currentClient().subscribeToTopic(thisTopic, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(String message) {

                }
            });
        }else{
            CHClient.connectClientWithUserID(null, null, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    CHClient.currentClient().subscribeToTopic(thisTopic, new ChannelProcessComplete() {
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
        }
    }

}
