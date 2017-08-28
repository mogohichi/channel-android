package co.getchannel.channel.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 8/16/2017.
 */

public class Application {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
//    @SerializedName("data")
//    private String data;

    public Application(String name,String id,String createdAt){
        this.name = name;
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
