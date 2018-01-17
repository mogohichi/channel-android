package co.getchannel.channel;

/**
 * Created by Admin on 8/16/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

import co.getchannel.channel.activities.ChatActivity;
import co.getchannel.channel.callback.ChannelCallback;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.callback.ChannelProcessComplete;


public class Channel {

    private static Activity activity;
    private static Channel channelInstance = null; // the only instance of the class

    public static Channel getInstance() {
        if (channelInstance == null) {
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
        try {
            Channel.sharedPreferences = sharedPreferences;
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getPackageName() {
        return packageName;
    }

    public static void setPackageName(String packageName) {
        if (packageName != null)
            Channel.packageName = packageName;
    }

    public final static void setupApplicationContextWithApplicationKey(Context context, String applicationID, final ChannelCallback callback) {
        try {
            if (context == null) {
                callback.onFail("Application Context not found");
                return;
            }


            setPackageName(context.getPackageName());
            setSharedPreferences(context.getSharedPreferences("Channel", android.content.Context.MODE_PRIVATE));
            setupApplicationAndClient(applicationID, null, null, callback);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }
    }

    public final static void setupApplicationContextWithApplicationKeyAndUser(Context context, String applicationID, String userID, HashMap<String, String> userData, final ChannelCallback callback) {
        try {
            if (context == null) {
                callback.onFail("Application Context not found");
                return;
            }


            setPackageName(context.getPackageName());
            setSharedPreferences(context.getSharedPreferences("Channel", android.content.Context.MODE_PRIVATE));
            setupApplicationAndClient(applicationID, userID, userData, callback);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }
    }

    private static void setupApplicationAndClient(String applicationId, String userID, HashMap<String, String> userData, final ChannelCallback callback) {
        try {
            CHConfiguration.setApplicationId(applicationId);
            CHClient.currentClient().setUserID(userID);
            CHClient.currentClient().setUserData(userData);
            CHClient.connectClientWithUserID(userID, userData, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });
        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }
    }

    public final static void chatView(final Activity activity) {
        try {
            if (CHClient.currentClient() == null) {
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                return;
            }

            if (CHConfiguration.getApplicationId() == null) {
                return;
            }

            if (CHConfiguration.getApplicationId().length() == 0) {
                return;
            }


            Intent myIntent = new Intent(activity, ChatActivity.class);
            activity.startActivity(myIntent);
        } catch (Exception e) {

        }
    }

    public final static void chatViewWithUserID(final Activity activity, String userID, HashMap<String, String> userData) {
        try {
            if (CHClient.currentClient() == null) {
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                return;
            }

            if (CHConfiguration.getApplicationId() == null) {
                return;
            }

            if (CHConfiguration.getApplicationId().length() == 0) {
                return;
            }


            Intent myIntent = new Intent(activity, ChatActivity.class);
            myIntent.putExtra("userData", userData);
            myIntent.putExtra("userID", userID);
            activity.startActivity(myIntent);
        } catch (Exception e) {

        }
    }

    public final static void showLatestNotification(final Activity activity, final ChannelCallback callback) {
        try {
            if (CHClient.currentClient() == null) {
                callback.onFail("Current Client not found");
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                callback.onFail("Current Client not found");
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                callback.onFail("Current Client not found");
                return;
            }


            CHClient.currentClient().checkNewNotification(activity, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });
        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }

    public final static void saveDeviceToken(String token, final ChannelCallback callback) {
        try {
            if (CHClient.currentClient() == null) {
                callback.onFail("Current Client not found");
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                callback.onFail("Current Client not found");
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                callback.onFail("Current Client not found");
                return;
            }


            CHClient.currentClient().saveDeviceToken(token, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });

        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }

    public final static void subscribeToTopic(String topic, final ChannelCallback callback) {
        try {
            if (CHClient.currentClient() == null) {
                callback.onFail("Current Client not found");
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                callback.onFail("Current Client not found");
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                callback.onFail("Current client not found");
                return;
            }


            CHClient.currentClient().subscribeToTopic(topic, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });
        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }
    }

    public final static void unsubscribeFromTopic(String topic, final ChannelCallback callback) {
        try {
            if (CHClient.currentClient() == null) {
                callback.onFail("Current Client not found");
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                callback.onFail("Current Client not found");
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                callback.onFail("Current Client not found");
                return;
            }


            CHClient.currentClient().subscribeToTopic(topic, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });
        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }

    }

    public final static void postbackPushNotification(Map<String, String> data, final ChannelCallback callback) {
        try {
            if (CHClient.currentClient() == null) {
                callback.onFail("Current Client not found");
                return;
            }
            if (CHClient.currentClient().getClientID() == null) {
                callback.onFail("Current Client not found");
                return;
            }

            if (CHClient.currentClient().getClientID().length() == 0) {
                callback.onFail("Current Client not found");
                return;
            }


            CHClient.currentClient().postbackPushNotification(data, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFail(String message) {
                    callback.onFail(message);
                }
            });

        } catch (Exception e) {
            callback.onFail(e.getMessage());
            return;
        }
    }
}
