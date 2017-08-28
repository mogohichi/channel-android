package co.getchannel.channel.api;

import co.getchannel.channel.models.internal.Client;
import co.getchannel.channel.models.internal.Message;
import co.getchannel.channel.models.internal.MessageData;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import co.getchannel.channel.responses.CHClientResponse;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Admin on 8/16/2017.
 */

public interface CHAPIInterface {

    @GET("app/info")
    Call<CHApplicationInfoResponse> getApplicationInfo();

    @POST("client")
    Call<CHClientResponse> connectClient(@Body Client client);

    @PUT("client")
    Call<CHClientResponse> updateCllentData(@Body Client client);


    @GET("thread/messages")
    Call<CHThreadResponse> activeThread();

    @POST("thread/messages")
    Call<CHMessageResponse> sendMessage(@Body MessageData data);
}
