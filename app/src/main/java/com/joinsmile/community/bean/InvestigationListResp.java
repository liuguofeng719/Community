package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/25.
 */
public class InvestigationListResp<T> extends BaseInfoVo {

    @SerializedName("InvestigationList")
    private T investigationList;

    public T getInvestigationList() {
        return investigationList;
    }

    public void setInvestigationList(T investigationList) {
        this.investigationList = investigationList;
    }
}
