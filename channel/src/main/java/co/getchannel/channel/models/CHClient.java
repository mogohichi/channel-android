package co.getchannel.channel.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.Channel;
import co.getchannel.channel.R;
import co.getchannel.channel.callback.ChannelProcessComplete;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.callback.UploadMessageImageComplete;
import co.getchannel.channel.api.CHAPI;
import co.getchannel.channel.api.CHAPIInterface;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.internal.Agent;
import co.getchannel.channel.models.internal.Application;
import co.getchannel.channel.models.internal.ButtonData;
import co.getchannel.channel.models.internal.Client;
import co.getchannel.channel.models.internal.Device;
import co.getchannel.channel.models.internal.ImageData;
import co.getchannel.channel.models.internal.Message;
import co.getchannel.channel.models.internal.MessageData;
import co.getchannel.channel.models.internal.Topic;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import co.getchannel.channel.responses.CHClientResponse;
import co.getchannel.channel.responses.CHEmptyResponse;
import co.getchannel.channel.responses.CHMessageImageResponse;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHNotificationResponse;
import co.getchannel.channel.responses.CHThreadResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 8/21/2017.
 */

public class CHClient {
    public static String clientID;
    public static String userID;
    public static HashMap<String,String> userData;
    private static CHClient _client = null;
    public static CHClient currentClient()
    {
        if (_client == null)
            _client = new CHClient();
        return _client;
    }

    public static String getClientID() {
        SharedPreferences sharedPref = Channel.getSharedPreferences();
        return sharedPref.getString("CH_CHANNEL_ID" + CHConfiguration.getApplicationId(),clientID);
    }

    public final static void setClientID(String clientID) {
            SharedPreferences sharedPref = Channel.getSharedPreferences();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("CH_CHANNEL_ID" + CHConfiguration.getApplicationId(), clientID);
            editor.commit();
    }


    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        CHClient.userID = userID;
    }

    public static HashMap<String, String> getUserData() {
        return userData;
    }

    public static void setUserData(HashMap<String, String> userData) {
        CHClient.userData = userData;
    }

    public static void applicationInfo( final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHApplicationInfoResponse> call = apiService.getApplicationInfo();
        call.enqueue(new Callback<CHApplicationInfoResponse>() {
            @Override
            public void onResponse(Call<CHApplicationInfoResponse> call, Response<CHApplicationInfoResponse> response) {
                if (response.code() == 200){
                    List<Agent> agents = response.body().getResult().getData().getAgents();
                    Application app = response.body().getResult().getData().getApplication();
                    Log.d(CHConstants.kChannel_tag,"Number of agent received: " + agents.size());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHApplicationInfoResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static  HashMap<String,String> deviceInfo(){
        HashMap<String,String> deviceInfo = new HashMap<String,String>();
        deviceInfo.put("OS Version",System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")");
        deviceInfo.put("OS API Level",android.os.Build.VERSION.SDK_INT + "");
        deviceInfo.put("Language", Locale.getDefault().getDisplayLanguage());
        deviceInfo.put("Device",android.os.Build.DEVICE);
        deviceInfo.put("ProcessName",Channel.getPackageName());
        deviceInfo.put("SystemName","Android");
        deviceInfo.put("SystemVersion", Build.VERSION.RELEASE);
        deviceInfo.put("SystemAPILevel",android.os.Build.VERSION.SDK_INT + "");
        deviceInfo.put("SystemDeviceTypeFormatted",android.os.Build.DEVICE);
        deviceInfo.put("DeviceModel",android.os.Build.MODEL );
        return  deviceInfo;
    }

    public static void connectClientWithUserID(String userID,HashMap<String,String> userData, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Client client = new Client();
        if (userID != null){
            client.setUserID(userID);

        }
        if(userData != null){
            client.setUserData(userData);
        }
        HashMap<String,String> deviceInfo = new HashMap<String,String>();
        client.setDeviceInfo(CHClient.deviceInfo());

        Call<CHClientResponse> call = apiService.connectClient(client);


        call.enqueue(new Callback<CHClientResponse>() {
            @Override
            public void onResponse(Call<CHClientResponse> call, Response<CHClientResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, response.body().getResult().getData().getClientID());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHClientResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void updateClientData(String userID, HashMap<String,String> userData, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Client client = new Client();
        if (userID != null){
            client.setUserID(userID);

        }
        if(userData != null){
            client.setUserData(userData);
        }
        HashMap<String,String> deviceInfo = new HashMap<String,String>();
        client.setDeviceInfo(CHClient.deviceInfo());

        Call<CHClientResponse> call = apiService.updateCllentData(client);
        call.enqueue(new Callback<CHClientResponse>() {
            @Override
            public void onResponse(Call<CHClientResponse> call, Response<CHClientResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "updated " + response.body().getResult().getData().getClientID());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHClientResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }


    public static void activeThread(final ThreadFetchComplete fetchComplete, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHThreadResponse> call = apiService.activeThread();
        call.enqueue(new Callback<CHThreadResponse>() {
            @Override
            public void onResponse(Call<CHThreadResponse> call, Response<CHThreadResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "activeThread " + response.body().getResult().getData());
                    fetchComplete.complete(response.body());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHThreadResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void sendImage(final SendMessageComplete sentComplete, Message message, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHMessageResponse> call = apiService.sendMessage(message);
        call.enqueue(new Callback<CHMessageResponse>() {
            @Override
            public void onResponse(Call<CHMessageResponse> call, Response<CHMessageResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "activeThread " + response.body());
                    sentComplete.complete(response.body());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHMessageResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }
    public static void sendMessage(final SendMessageComplete sentComplete, Message message, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        MessageData md = new MessageData();
        md.setData(message);
        Call<CHMessageResponse> call = apiService.sendMessage(md);
        call.enqueue(new Callback<CHMessageResponse>() {
            @Override
            public void onResponse(Call<CHMessageResponse> call, Response<CHMessageResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "activeThread " + response.body());
                    sentComplete.complete(response.body());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHMessageResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void uploadMessageImage(final UploadMessageImageComplete uploadComplete, String imageBase64, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        ImageData img = new ImageData();
        img.setImageBase64(imageBase64);
        Call<CHMessageImageResponse> call = apiService.uploadMessageImage(img);
        call.enqueue(new Callback<CHMessageImageResponse>() {
            @Override
            public void onResponse(Call<CHMessageImageResponse> call, Response<CHMessageImageResponse> response) {
                if (response.code() == 200){
                    uploadComplete.complete(response.body());
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHMessageImageResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void checkNewNotification(final Activity activity, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHNotificationResponse> call = apiService.notification();
        call.enqueue(new Callback<CHNotificationResponse>() {
            @Override
            public void onResponse(Call<CHNotificationResponse> call, Response<CHNotificationResponse> response) {
                if (response.code() == 200){

//                    notificationFetchComplete.complete(response.body());
                    final CHNotificationResponse data = response.body();

                    if (data.getResult().getData().getData() != null){
                        final String cover = data.getResult().getData().getData().getNotification().getPayload().getImageURL();
                        final String title = data.getResult().getData().getData().getNotification().getPayload().getText();
                        Thread readThread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View customView = inflater.inflate(R.layout.custom_dialog, null);

                                TextView customText = (TextView) customView.findViewById(R.id.custom_text_view);
                                customText.setText(title == null ? "" : title);
                                customText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                Button leftButton = (Button) customView.findViewById(R.id.left_button);
                                Button rightButton = (Button) customView.findViewById(R.id.right_button);

                                final MaterialStyledDialog dialog =   new MaterialStyledDialog.Builder(activity)
                                        .setStyle(Style.HEADER_WITH_TITLE)
//                                        .setHeaderDrawable(d)
                                        .setCustomView(customView,20,20,20,20)
                                        .withDialogAnimation(true)
                                        .setHeaderScaleType(ImageView.ScaleType.FIT_CENTER).build();

                                if(cover != null)
                                     Picasso.with(activity).load(cover).into(dialog.getDialogHead());

                                if (data.getResult().getData().getData().getNotification().getPayload().getButtons().size() < 2) {
                                    rightButton.setVisibility(View.GONE);
                                }else{
                                    final CHNotificationResponse.CHNotificationPayloadButton left =  data.getResult().getData().getData().getNotification().getPayload().getButtons().get(0);
                                    final CHNotificationResponse.CHNotificationPayloadButton right =  data.getResult().getData().getData().getNotification().getPayload().getButtons().get(1);


                                    ColorStateList leftState = new ColorStateList(new int[][] {
                                            new int[] { android.R.attr.state_enabled}, // enabled
                                    }, new int[] {
                                            left.getBackgroundColor() == null ? Color.parseColor("#0080FF") :
                                                    Color.parseColor(left.getBackgroundColor()),
                                    });
                                    leftButton.setTransformationMethod(null);
                                    leftButton.setBackgroundTintList(leftState);
                                    leftButton.setTextColor(left.getTextColor() == null ? Color.parseColor("#FFFFFF") :
                                            Color.parseColor(left.getTextColor()));
                                    leftButton.setText(left.getTitle());
                                    leftButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            CHClient.currentClient().postbackNotification(data.getResult().getData().getPublicID(), left, new ChannelProcessComplete() {
                                                @Override
                                                public void onSuccess() {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onFail(String message) {
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    });

                                    ColorStateList rightState = new ColorStateList(new int[][] {
                                            new int[] { android.R.attr.state_enabled}, // enabled
                                    }, new int[] {
                                            right.getBackgroundColor() == null ? Color.parseColor("#0080FF") :
                                                    Color.parseColor(right.getBackgroundColor()),
                                    });
                                    rightButton.setTransformationMethod(null);
                                    rightButton.setBackgroundTintList(rightState);
                                    rightButton.setTextColor(right.getTextColor() == null ? Color.parseColor("#FFFFFF") :
                                            Color.parseColor(right.getTextColor()));
                                    rightButton.setText(right.getTitle());
                                    rightButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            CHClient.currentClient().postbackNotification(data.getResult().getData().getPublicID(), right, new ChannelProcessComplete() {
                                                @Override
                                                public void onSuccess() {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onFail(String message) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                                dialog.show();
                            }
                        });
                        readThread.run();
                        callback.onSuccess();
                    }
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHNotificationResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void postbackNotification(String notificationID,CHNotificationResponse.CHNotificationPayloadButton button, final ChannelProcessComplete callback){

        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);

        ButtonData data = new ButtonData();
        data.setButton(button);

        Call<CHNotificationResponse> call = apiService.notificationAction(notificationID,data);
        call.enqueue(new Callback<CHNotificationResponse>() {
            @Override
            public void onResponse(Call<CHNotificationResponse> call, Response<CHNotificationResponse> response) {
                if (response.code() == 200){
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHNotificationResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void saveDeviceToken(String token, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Device device = new Device();
        device.setToken(token);
        device.setInfo(CHClient.deviceInfo());

        Call<CHEmptyResponse> call = apiService.saveDeviceToken(device);
        call.enqueue(new Callback<CHEmptyResponse>() {
            @Override
            public void onResponse(Call<CHEmptyResponse> call, Response<CHEmptyResponse> response) {
                if (response.code() == 200){
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHEmptyResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void postbackPushNotification(Map<String, String> data, final ChannelProcessComplete callback){

        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHEmptyResponse> call = apiService.trackNotificationOpen(data.get("id"));
        call.enqueue(new Callback<CHEmptyResponse>() {
            @Override
            public void onResponse(Call<CHEmptyResponse> call, Response<CHEmptyResponse> response) {
                if (response.code() == 200){
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHEmptyResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }


    public static void subscribeToTopic(String topic, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Topic subscribeTopic = new Topic();
        subscribeTopic.setTopic(topic);
        Call<CHEmptyResponse> call = apiService.subscribeToTopic(subscribeTopic);
        call.enqueue(new Callback<CHEmptyResponse>() {
            @Override
            public void onResponse(Call<CHEmptyResponse> call, Response<CHEmptyResponse> response) {
                if (response.code() == 200){
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHEmptyResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }

    public static void unsubscribeFromTopic(String topic, final ChannelProcessComplete callback){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Topic subscribeTopic = new Topic();
        subscribeTopic.setTopic(topic);
        Call<CHEmptyResponse> call = apiService.subscribeToTopic(subscribeTopic);
        call.enqueue(new Callback<CHEmptyResponse>() {
            @Override
            public void onResponse(Call<CHEmptyResponse> call, Response<CHEmptyResponse> response) {
                if (response.code() == 200){
                    callback.onSuccess();
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                    callback.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<CHEmptyResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
                callback.onFail(t.toString());
            }
        });
    }
}
