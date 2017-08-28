package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rataphon on 8/25/2017 AD.
 */

public class CHMessageResponse {
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("ID")
    private String ID;
    @SerializedName("isFromBusiness")
    private Boolean isFromBusiness;
    @SerializedName("isOwner")
    private Boolean isOwner;
    @SerializedName("isNewMessage")
    private Boolean isNewMessage;
    @SerializedName("data")
    private CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageData data;
    @SerializedName("sender")
    private CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageSender sender;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Boolean getFromBusiness() {
        return isFromBusiness;
    }

    public void setFromBusiness(Boolean fromBusiness) {
        isFromBusiness = fromBusiness;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public Boolean getNewMessage() {
        return isNewMessage;
    }

    public void setNewMessage(Boolean newMessage) {
        isNewMessage = newMessage;
    }

    public CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageData getData() {
        return data;
    }

    public void setData(CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageData data) {
        this.data = data;
    }

    public CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageSender getSender() {
        return sender;
    }

    public void setSender(CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage.CHThreadMessageSender sender) {
        this.sender = sender;
    }

    public class CHThreadMessageData{
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    public class CHThreadMessageSender{
        @SerializedName("clientID")
        private String clientID;
        @SerializedName("name")
        private String name;
        @SerializedName("profilePictureURL")
        private String profilePictureURL;

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePictureURL() {
            return profilePictureURL;
        }

        public void setProfilePictureURL(String profilePictureURL) {
            this.profilePictureURL = profilePictureURL;
        }
    }

}
