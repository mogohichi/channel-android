package co.getchannel.channel.models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.Channel;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.api.CHAPI;
import co.getchannel.channel.api.CHAPIInterface;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.internal.Agent;
import co.getchannel.channel.models.internal.Application;
import co.getchannel.channel.models.internal.Client;
import co.getchannel.channel.models.internal.Message;
import co.getchannel.channel.models.internal.MessageData;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import co.getchannel.channel.responses.CHClientResponse;
import co.getchannel.channel.responses.CHMessageResponse;
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
        String clientID = "";
        Activity act = Channel.getActivity();
        if (act != null) {
            SharedPreferences sharedPref = act.getPreferences(android.content.Context.MODE_PRIVATE);
            clientID = sharedPref.getString("CH_CHANNEL_ID" + CHConfiguration.getApplicationId(),clientID);
        }
        return clientID;
    }

    public final static void setClientID(String clientID) {
        Activity act = Channel.getActivity();
        if (act != null) {
            SharedPreferences sharedPref = act.getPreferences(android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("CH_CHANNEL_ID" + CHConfiguration.getApplicationId(), clientID);
            editor.commit();
        }
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

    public static void applicationInfo(){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHApplicationInfoResponse> call = apiService.getApplicationInfo();
        call.enqueue(new Callback<CHApplicationInfoResponse>() {
            @Override
            public void onResponse(Call<CHApplicationInfoResponse> call, Response<CHApplicationInfoResponse> response) {
                if (response.code() == 200){
                    List<Agent> agents = response.body().getResult().getData().getAgents();
                    Application app = response.body().getResult().getData().getApplication();
                    Log.d(CHConstants.kChannel_tag,"Number of agent received: " + agents.size());
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                }
            }

            @Override
            public void onFailure(Call<CHApplicationInfoResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
            }
        });
    }

    public static void connectClientWithUserID(String userID,HashMap<String,String> userData){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Client client = new Client();
        if (userID != null){
            client.setUserID(userID);

        }
        if(userData != null){
            client.setUserData(userData);
        }
        HashMap<String,String> deviceInfo = new HashMap<String,String>();
        deviceInfo.put("OS Version",System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")");
        deviceInfo.put("OS API Level",android.os.Build.VERSION.SDK_INT + "");
        deviceInfo.put("Device",android.os.Build.DEVICE);
        deviceInfo.put("Device",android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")");
        client.setDeviceInfo(deviceInfo);

        Call<CHClientResponse> call = apiService.connectClient(client);
        call.enqueue(new Callback<CHClientResponse>() {
            @Override
            public void onResponse(Call<CHClientResponse> call, Response<CHClientResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, response.body().getResult().getData().getClientID());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                }
            }

            @Override
            public void onFailure(Call<CHClientResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
            }
        });
    }

    public static void updateClientData(String userID,HashMap<String,String> userData){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Client client = new Client();
        if (userID != null){
            client.setUserID(userID);

        }
        if(userData != null){
            client.setUserData(userData);
        }
        HashMap<String,String> deviceInfo = new HashMap<String,String>();
        deviceInfo.put("OS Version",System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")");
        deviceInfo.put("OS API Level",android.os.Build.VERSION.SDK_INT + "");
        deviceInfo.put("Device",android.os.Build.DEVICE);
        deviceInfo.put("Device",android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")");
        client.setDeviceInfo(deviceInfo);

        Call<CHClientResponse> call = apiService.updateCllentData(client);
        call.enqueue(new Callback<CHClientResponse>() {
            @Override
            public void onResponse(Call<CHClientResponse> call, Response<CHClientResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "updated " + response.body().getResult().getData().getClientID());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                }
            }

            @Override
            public void onFailure(Call<CHClientResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
            }
        });
    }


    public static void activeThread(final ThreadFetchComplete fetchComplete){
        CHAPIInterface apiService = CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
        Call<CHThreadResponse> call = apiService.activeThread();
        call.enqueue(new Callback<CHThreadResponse>() {
            @Override
            public void onResponse(Call<CHThreadResponse> call, Response<CHThreadResponse> response) {
                if (response.code() == 200){
                    Log.d(CHConstants.kChannel_tag, "activeThread " + response.body().getResult().getData());
                    fetchComplete.complete(response.body());
                    CHClient.currentClient().setClientID(response.body().getResult().getData().getClientID());
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                }
            }

            @Override
            public void onFailure(Call<CHThreadResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
            }
        });
    }

    public static void sendMessage(final SendMessageComplete sentComplete, Message message){


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
                }else{
                    Log.d(CHConstants.kChannel_tag, response.message());
                }
            }

            @Override
            public void onFailure(Call<CHMessageResponse>call, Throwable t) {
                // Log error here since request failed
                Log.d(CHConstants.kChannel_tag,t.toString());
            }
        });
    }
}
