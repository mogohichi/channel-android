package co.getchannel.channel.callback;

/**
 * Created by Admin on 12/25/2017.
 */

public interface ChannelProcessComplete {
    void onSuccess();
    void onFail(String message);
}
