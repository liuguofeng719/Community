package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/21.
 */
public class ReceiverAddressVo {

    @SerializedName("userID")
    private String userID;  //用户ID
    @SerializedName("countyID")
    private String countyID; //区县ID
    @SerializedName("Street")
    private String street; //街道
    @SerializedName("linkman")
    private String linkman;//联系人
    @SerializedName("phoneNumber")
    private String phoneNumber;//电话
    @SerializedName("isDefault")
    private int isDefault; //是否为默认收货地址 (int类型, 0:否 1:是）

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCountyID() {
        return countyID;
    }

    public void setCountyID(String countyID) {
        this.countyID = countyID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
