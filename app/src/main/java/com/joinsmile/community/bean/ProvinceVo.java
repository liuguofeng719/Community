package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/8/7.
 * 省级
 */
public class ProvinceVo implements Serializable {

    @SerializedName("ProvinceID")
    private String provinceId;
    @SerializedName("ProvinceName")
    private String provinceName;
    @SerializedName("FirstLatter")
    private String firstLatter;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getFirstLatter() {
        return firstLatter;
    }

    public void setFirstLatter(String firstLatter) {
        this.firstLatter = firstLatter;
    }
}
