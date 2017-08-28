package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHClientResponse {
    public class CHClientResult{
        public class CHClientData{
            @SerializedName("clientID")
            private String clientID;

            public String getClientID() {
                return clientID;
            }

            public void setClientID(String clientID) {
                this.clientID = clientID;
            }
        }
        @SerializedName("data")
        private CHClientData data;

        public CHClientData getData() {
            return data;
        }

        public void setData(CHClientData data) {
            this.data = data;
        }
    }



    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private CHClientResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CHClientResult getResult() {
        return result;
    }

    public void setResult(CHClientResult result) {
        this.result = result;
    }
}
