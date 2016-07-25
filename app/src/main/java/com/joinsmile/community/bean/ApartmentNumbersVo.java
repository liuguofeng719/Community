package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class ApartmentNumbersVo implements Serializable {

    @SerializedName("NumberID")
    private String numberID;
    @SerializedName("NumberName")
    private String numberName;//门牌全名称
    @SerializedName("IsDefault")
    private int isDefault;//IsDefault是否是默认房间  0:否  1:是
    @SerializedName("City")
    private String city ;//城市名称
    @SerializedName("Building")
    private String building ;//小区名称
    @SerializedName("Unit")
    private String unit ;//单元名称
    @SerializedName("Apartment")
    private String apartment; //门牌号名称

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public int isDefault() {
        return isDefault;
    }

    public void setDefault(int aDefault) {
        isDefault = aDefault;
    }

    public String getNumberID() {
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }

    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }
}
