package co.getchannel.channel.api;

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
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @POST("thread/messages")
    Call<CHMessageResponse> sendMessage(@Body Message data);


    @POST("thread/messages/upload")
    Call<CHMessageImageResponse> uploadMessageImage(@Body ImageData data);

    @GET("notification")
    Call<CHNotificationResponse> notification();

    @POST("notification/{notificationID}")
    Call<CHNotificationResponse> notificationAction(@Path("notificationID") String notificationID, @Body ButtonData data);

    @POST("notification/{notificationID}/open/push")
    Call<CHEmptyResponse> trackNotificationOpen(@Path("notificationID") String notificationID);

    @POST("client/device")
    Call<CHEmptyResponse> saveDeviceToken(@Body Device device);

    @POST("client/topics")
    Call<CHEmptyResponse> subscribeToTopic(@Body Topic topic);

    @DELETE("client/topics")
    Call<CHEmptyResponse> unsubscribeFromTopic(@Body Topic topic);

}
