package co.getchannel.channel.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.getchannel.channel.models.Agent;
import co.getchannel.channel.models.Application;

/**
 * Created by Admin on 8/16/2017.
 */

public class CHApplicationInfoResponse {
    public class CHApplicationInfoResult{
        @SerializedName("data")
        private CHApplicationInfoData data;

        public CHApplicationInfoData getData() {
            return data;
        }

        public void setData(CHApplicationInfoData data) {
            this.data = data;
        }
    }

    public class CHApplicationInfoData{
        @SerializedName("agents")
        private List<Agent> agents;
        @SerializedName("application")
        private Application application;

        public List<Agent> getAgents() {
            return agents;
        }

        public void setAgents(List<Agent> agents) {
            this.agents = agents;
        }

        public Application getApplication() {
            return application;
        }

        public void setApplication(Application application) {
            this.application = application;
        }
    }

    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private CHApplicationInfoResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CHApplicationInfoResult getResult() {
        return result;
    }

    public void setResult(CHApplicationInfoResult result) {
        this.result = result;
    }
}
