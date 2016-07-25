package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class RepairAndComplaintsVo implements Serializable {

    @SerializedName("Title")
    private String title; //标题
    @SerializedName("Description")
    private String description;  //描述
    @SerializedName("BuildingName")
    private String buildingName;  //小区名
    @SerializedName("UnitName")
    private String unitName; //单元名称
    @SerializedName("ApartmentNumber")
    private String apartmentNumber; //门牌号
    @SerializedName("LinkmanPhoneNumber")
    private String linkmanPhoneNumber; //联系人电话
    @SerializedName("Picture1")
    private String picture1;
    @SerializedName("Picture2")
    private String picture2;
    @SerializedName("Picture3")
    private String picture3;
    @SerializedName("Picture4")
    private String picture4;
    @SerializedName("Picture5")
    private String picture5;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getLinkmanPhoneNumber() {
        return linkmanPhoneNumber;
    }

    public void setLinkmanPhoneNumber(String linkmanPhoneNumber) {
        this.linkmanPhoneNumber = linkmanPhoneNumber;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getPicture4() {
        return picture4;
    }

    public void setPicture4(String picture4) {
        this.picture4 = picture4;
    }

    public String getPicture5() {
        return picture5;
    }

    public void setPicture5(String picture5) {
        this.picture5 = picture5;
    }

    @Override
    public String toString() {
        return "RepairAndComplaintsVo{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", linkmanPhoneNumber='" + linkmanPhoneNumber + '\'' +
                ", picture1='" + picture1 + '\'' +
                ", picture2='" + picture2 + '\'' +
                ", picture3='" + picture3 + '\'' +
                ", picture4='" + picture4 + '\'' +
                ", picture5='" + picture5 + '\'' +
                '}';
    }
}
