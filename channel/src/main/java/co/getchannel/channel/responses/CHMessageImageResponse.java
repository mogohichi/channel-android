package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rataphon on 9/4/2017 AD.
 */

public class CHMessageImageResponse {
    public class CHMessageImageResult{
        public class CHMessageImageData{
            @SerializedName("url")
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
        @SerializedName("data")
        private CHMessageImageData data;

        public CHMessageImageData getData() {
            return data;
        }

        public void setData(CHMessageImageData data) {
            this.data = data;
        }
    }
    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private CHMessageImageResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CHMessageImageResult getResult() {
        return result;
    }

    public void setResult(CHMessageImageResult result) {
        this.result = result;
    }

}
