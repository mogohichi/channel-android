package co.getchannel.channel.models.internal;

import java.util.HashMap;

/**
 * Created by rataphon on 9/23/2017 AD.
 */

public class Device {
    private String token;
    private HashMap<String,String> info;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }
}
