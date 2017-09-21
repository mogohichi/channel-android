package co.getchannel.channel.models.internal;

import co.getchannel.channel.responses.CHNotificationResponse;

/**
 * Created by rataphon on 9/21/2017 AD.
 */

public class ButtonData {
    private CHNotificationResponse.CHNotificationPayloadButton data;

    public CHNotificationResponse.CHNotificationPayloadButton getButton() {
        return data;
    }

    public void setButton(CHNotificationResponse.CHNotificationPayloadButton data) {
        this.data = data;
    }
}