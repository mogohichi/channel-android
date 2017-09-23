package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rataphon on 9/23/2017 AD.
 */

public class CHEmptyResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
