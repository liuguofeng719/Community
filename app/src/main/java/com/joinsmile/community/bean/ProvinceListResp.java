package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

public class ProvinceListResp<T> extends BaseInfoVo {

    @SerializedName("ProvinceList")
    private T provinceList;

    public T getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(T provinceList) {
        this.provinceList = provinceList;
    }
}
