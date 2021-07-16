package com.tahir7008.ibmessanger.Models;

public class Status {
    private  String ImgUrl;
    private  long timeStamp;

    public Status(String imgUrl, long timeStamp) {
        ImgUrl = imgUrl;
        this.timeStamp = timeStamp;
    }

    public Status() {
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
