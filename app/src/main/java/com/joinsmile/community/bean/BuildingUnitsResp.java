package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class BuildingUnitsResp<T> extends BaseInfoVo {

    @SerializedName("BuildingUnitList")
    private T buildingUnitList;

    public T getBuildingUnitList() {
        return buildingUnitList;
    }

    public void setBuildingUnitList(T buildingUnitList) {
        this.buildingUnitList = buildingUnitList;
    }
}
