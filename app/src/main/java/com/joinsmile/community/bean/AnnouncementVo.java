package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class AnnouncementVo implements Serializable{

    @SerializedName("AnnouncementID")
    private String announcementID;// 公告ID
    @SerializedName("BuildingID")
    private String buildingID; //小区ID
    @SerializedName("BuildingName")
    private String buildingName; //小区名称
    @SerializedName("Title")
    private String title; //公告标题
    @SerializedName("Content")
    private String content; //公告内容
    @SerializedName("PublishDate")
    private String publishDate; //发布时间

    public String getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(String announcementID) {
        this.announcementID = announcementID;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
