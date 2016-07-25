package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class ApartmentNumbersResp<T> extends BaseInfoVo {

    @SerializedName("ApartmentNumberList")
    private T apartmentNumberList;

    public T getApartmentNumberList() {
        return apartmentNumberList;
    }

    public void setApartmentNumberList(T apartmentNumberList) {
        this.apartmentNumberList = apartmentNumberList;
    }
}
