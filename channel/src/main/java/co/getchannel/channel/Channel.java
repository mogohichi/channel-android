package co.getchannel.channel;

/**
 * Created by Admin on 8/16/2017.
 */

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;



import co.getchannel.channel.api.CHAPI;
import co.getchannel.channel.api.CHAPIInterface;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.Agent;
import co.getchannel.channel.models.Application;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public final static void setupActivityWithApplicationId(final WeakReference<Activity> mReference,String applicationId){
      Activity act = mReference.get();
        if (act != null) {
            activity = act;
            CHConfiguration.setApplicationId(applicationId);
       }
        Log.d(kChannel_tag,CHConfiguration.getApplicationId());
    }



    public static void applicationInfo(){
        CHAPIInterface apiService =
                CHAPI.getAPIWithApplication().create(CHAPIInterface.class);
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
                String x = t.toString();
                Log.d(CHConstants.kChannel_tag,x);
            }
        });
    }
}
