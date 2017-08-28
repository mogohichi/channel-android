package co.getchannel.channel.api;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.Channel;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Headers;

/**
 * Created by Admin on 8/16/2017.
 */

public interface CHAPIInterface {

    @GET("app/info")
    Call<CHApplicationInfoResponse> getApplicationInfo();

}