package co.getchannel.channel.callback;

import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;

/**
 * Created by rataphon on 8/25/2017 AD.
 */

public interface SendMessageComplete {
    public void complete(CHMessageResponse data);
}
