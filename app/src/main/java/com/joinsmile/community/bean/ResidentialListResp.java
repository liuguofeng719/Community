package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class ResidentialListResp<T> extends BaseInfoVo {

    @SerializedName("BuildingList")
    private T buildingList;

    public T getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(T buildingList) {
        this.buildingList = buildingList;
    }
}
