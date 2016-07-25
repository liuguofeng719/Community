package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class ResidentialBuildingVo implements Serializable {

    @SerializedName("BuildingID")
    private String buildingID;
    @SerializedName("BuildingName")
    private String buildingName;
    @SerializedName("Linkman")
    private String linkman;
    @SerializedName("TelephoneNumber")
    private String telephoneNumber;
    @SerializedName("Street")
    private String street;
    @SerializedName("PropertyCompanyName")
    private String propertyCompanyName;
    @SerializedName("PropertyCompanyID")
    private String propertyCompanyID;
    @SerializedName("IsOpen")
    private boolean isOpen;
    @SerializedName("IsCreatedPlatformUser")
    private boolean isCreatedPlatformUser;

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPropertyCompanyName() {
        return propertyCompanyName;
    }

    public void setPropertyCompanyName(String propertyCompanyName) {
        this.propertyCompanyName = propertyCompanyName;
    }

    public String getPropertyCompanyID() {
        return propertyCompanyID;
    }

    public void setPropertyCompanyID(String propertyCompanyID) {
        this.propertyCompanyID = propertyCompanyID;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isCreatedPlatformUser() {
        return isCreatedPlatformUser;
    }

    public void setCreatedPlatformUser(boolean createdPlatformUser) {
        isCreatedPlatformUser = createdPlatformUser;
    }
}
