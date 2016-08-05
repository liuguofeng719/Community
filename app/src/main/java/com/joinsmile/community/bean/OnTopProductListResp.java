package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

public class OnTopProductListResp<T> extends BaseInfoVo {

    @SerializedName("OnTopProductList")
    public T onTopProductList;

    public T getOnTopProductList() {
        return onTopProductList;
    }

    public void setOnTopProductList(T onTopProductList) {
        this.onTopProductList = onTopProductList;
    }

    @Override
    public String toString() {
        return "OnTopProductListResp{" +
                "onTopProductList=" + onTopProductList +
                '}';
    }
}
