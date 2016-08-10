package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class RepairAndComplaintDetailVo implements Serializable {

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
    @SerializedName("UploadDate") //上报时间
    private String uploadDate;
    @SerializedName("Pictures")//图片列表
    private ArrayList<String> pictures;

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

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
}
