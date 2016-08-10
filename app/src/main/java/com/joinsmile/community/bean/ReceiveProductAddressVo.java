package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/8/7.
 * 收货地址
 */
public class ReceiveProductAddressVo implements Serializable {

    @SerializedName("AddressID")
    private String addressID;// 地址ID
    @SerializedName("Linkman")
    private String linkman;// 联系人
    @SerializedName("PhoneNumber")
    private String phoneNumber;// 电话
    @SerializedName("Address")
    private String address;//  详细收货地址
    @SerializedName("CityID")
    private String cityId;//城市编码
    @SerializedName("CityName")
    private String cityName;//城市名字
    @SerializedName("IsDefault")
    private int isDefault;//  是否为默认地址

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
