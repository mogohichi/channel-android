package co.getchannel.channel.callback;

/**
 * Created by Admin on 1/8/2018.
 */

public interface ChannelCallback {
    void onSuccess();
    void onFail(String message);
}
