package co.getchannel.channel.callback;

import co.getchannel.channel.responses.CHNotificationResponse;

/**
 * Created by rataphon on 9/21/2017 AD.
 */

public interface NotificationFetchComplete {
    public void complete(CHNotificationResponse data);
}
