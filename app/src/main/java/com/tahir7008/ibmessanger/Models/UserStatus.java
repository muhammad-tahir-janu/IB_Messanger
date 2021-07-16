package com.tahir7008.ibmessanger.Models;

import java.util.ArrayList;

public class UserStatus {

    private  String name, profileImg;
    private long lastUpdate;

    private ArrayList<Status> statuses;

    public UserStatus() {


    }

    public UserStatus(String name, String profileImg, long lastUpdate, ArrayList<Status> statuses) {
        this.name = name;
        this.profileImg = profileImg;
        this.lastUpdate = lastUpdate;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
