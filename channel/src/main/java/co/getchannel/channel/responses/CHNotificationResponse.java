package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rataphon on 9/21/2017 AD.
 */

public class CHNotificationResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private CHNotificationResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CHNotificationResult getResult() {
        return result;
    }

    public void setResult(CHNotificationResult result) {
        this.result = result;
    }

    public class CHNotificationResult {

        @SerializedName("data")
        private CHNotificationResponseData data;


        public CHNotificationResponseData getData() {
            return data;
        }

        public void setData(CHNotificationResponseData data) {
            this.data = data;
        }
    }

    public class CHNotificationResponseData {
        @SerializedName("publicID")
        private String publicID;

        @SerializedName("data")
        private CHNotificationData data;

        @SerializedName("notification")
        private CHNotification notification;

        public String getPublicID() {
            return publicID;
        }

        public void setPublicID(String publicID) {
            this.publicID = publicID;
        }

        public CHNotificationData getData() {
            return data;
        }

        public void setData(CHNotificationData data) {
            this.data = data;
        }

        public CHNotification getNotification() {
            return notification;
        }

        public void setNotification(CHNotification notification) {
            this.notification = notification;
        }


    }

    public class CHNotificationData {

        @SerializedName("notification")
        private CHNotificationDataNotification notification;



        public CHNotificationDataNotification getNotification() {
            return notification;
        }

        public void setNotification(CHNotificationDataNotification notification) {
            this.notification = notification;
        }
    }

    public class CHNotification {
        @SerializedName("publicID")
        private String publicID;

        public String getPublicID() {
            return publicID;
        }

        public void setPublicID(String publicID) {
            this.publicID = publicID;
        }
    }

    public class CHNotificationDataNotification{
        @SerializedName("type")
        private String type;

        @SerializedName("payload")
        private CHNotificationPayload payload;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public CHNotificationPayload getPayload() {
            return payload;
        }

        public void setPayload(CHNotificationPayload payload) {
            this.payload = payload;
        }
    }

    public class CHNotificationPayload {

        @SerializedName("imageURL")
        private String imageURL;

        @SerializedName("text")
        private String text;

        @SerializedName("templateType")
        private String templateType;

        @SerializedName("buttons")
        private List<CHNotificationPayloadButton> buttons;



        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public List<CHNotificationPayloadButton> getButtons() {
            return buttons;
        }

        public void setButtons(List<CHNotificationPayloadButton> buttons) {
            this.buttons = buttons;
        }
    }

    public class CHNotificationPayloadButton {
        @SerializedName("title")
        private String title;

        @SerializedName("backgroundColor")
        private String backgroundColor;

        @SerializedName("textColor")
        private String textColor;

        @SerializedName("url")
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getURL() {
            return url;
        }

        public void setURL(String url) {
            this.url = url;
        }
    }
}

