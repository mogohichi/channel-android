package co.getchannel.channel.models.internal;

import java.util.HashMap;

/**
 * Created by Admin on 8/21/2017.
 */

public class Client {
    private String userID;
    private HashMap<String,String> userData;
    private HashMap<String,String> deviceInfo;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public HashMap<String, String> getUserData() {
        return userData;
    }

    public void setUserData(HashMap<String, String> userData) {
        this.userData = userData;
    }

    public HashMap<String, String> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(HashMap<String, String> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
