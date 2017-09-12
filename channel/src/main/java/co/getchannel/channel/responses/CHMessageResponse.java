package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

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
    private CHMessageData data;
    @SerializedName("sender")
    private CHMessageSender sender;

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

    public CHMessageData getData() {
        return data;
    }

    public void setData(CHMessageData data) {
        this.data = data;
    }

    public CHMessageSender getSender() {
        return sender;
    }

    public void setSender(CHMessageSender sender) {
        this.sender = sender;
    }

    public class CHMessageData{
        @SerializedName("text")
        private String text;
        @SerializedName("card")
        private CHCard card;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public CHCard getCard() {
            return card;
        }

        public void setCard(CHCard card) {
            this.card = card;
        }

        public class CHCard {
            @SerializedName("type")
            private String type;
            @SerializedName("payload")
            private CHPayload payload;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public CHPayload getPayload() {
                return payload;
            }

            public void setPayload(CHPayload payload) {
                this.payload = payload;
            }

            public class CHPayload {
                @SerializedName("url")
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

        }
    }
    public class CHMessageSender{
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
