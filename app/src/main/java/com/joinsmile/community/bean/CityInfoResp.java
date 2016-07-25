package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/2/25.
 */
public class CityInfoResp<T> extends BaseInfoVo {

    @SerializedName("City")
    private T city;

    public T getCity() {
        return city;
    }

    public void setCity(T city) {
        this.city = city;
    }
}
