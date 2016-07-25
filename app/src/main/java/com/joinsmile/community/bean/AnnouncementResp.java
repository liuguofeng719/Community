package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class AnnouncementResp extends BaseInfoVo {

    @SerializedName("Announcement")
    private AnnouncementVo announcement;

    public AnnouncementVo getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(AnnouncementVo announcement) {
        this.announcement = announcement;
    }
}
