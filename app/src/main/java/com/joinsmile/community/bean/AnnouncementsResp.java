package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class AnnouncementsResp<T> extends BaseInfoVo {

    @SerializedName("AnnouncementList")
    public T announcementList;

    public T getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(T announcementList) {
        this.announcementList = announcementList;
    }
}
