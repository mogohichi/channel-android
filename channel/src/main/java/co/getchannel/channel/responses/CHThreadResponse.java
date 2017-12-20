package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

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
            private List<CHMessageResponse> messages;

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

            public List<CHMessageResponse> getMessages() {
                return messages;
            }

            public void setMessages(List<CHMessageResponse> messages) {
                this.messages = messages;
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
