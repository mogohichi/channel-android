package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 8/22/2017.
 */

public class CHThreadResponse {
    public class CHThreadResult{
        @SerializedName("data")
        private CHThreadData data;

        public CHThreadData getData() {
            return data;
        }

        public void setData(CHThreadData data) {
            this.data = data;
        }

        public class CHThreadData{
            @SerializedName("ID")
            private String ID;
            @SerializedName("next")
            private String next;
            @SerializedName("clientID")
            private String clientID;
            @SerializedName("messages")
            private List<CHThreadMessage> messages;

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getNext() {
                return next;
            }

            public void setNext(String next) {
                this.next = next;
            }

            public String getClientID() {
                return clientID;
            }

            public void setClientID(String clientID) {
                this.clientID = clientID;
            }

            public List<CHThreadMessage> getMessages() {
                return messages;
            }

            public void setMessages(List<CHThreadMessage> messages) {
                this.messages = messages;
            }

            public class CHThreadMessage{

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
                private CHThreadMessageData data;
                @SerializedName("sender")
                private CHThreadMessageSender sender;

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

                public CHThreadMessageData getData() {
                    return data;
                }

                public void setData(CHThreadMessageData data) {
                    this.data = data;
                }

                public CHThreadMessageSender getSender() {
                    return sender;
                }

                public void setSender(CHThreadMessageSender sender) {
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

        }
    }





    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private CHThreadResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CHThreadResult getResult() {
        return result;
    }

    public void setResult(CHThreadResult result) {
        this.result = result;
    }
}
