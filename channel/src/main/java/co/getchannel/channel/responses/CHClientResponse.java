package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHClientResponse {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
