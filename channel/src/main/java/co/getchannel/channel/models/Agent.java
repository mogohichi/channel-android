package co.getchannel.channel.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 8/16/2017.
 */

public class Agent {
    @SerializedName("name")
    private String name;
    @SerializedName("profilePictureURL")
    private String profilePictureURL;

    public Agent(String name,String profilePictureURL){
        this.name = name;
        this.profilePictureURL = profilePictureURL;
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
