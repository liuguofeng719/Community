package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class ProductVo implements Serializable {

    @SerializedName("ProductID")
    private String productID;//商品ID
    @SerializedName("ProductName")
    private String productName;//商品名称
    @SerializedName("SupplierID")
    private String supplierID;//供应商ID
    @SerializedName("SupplierName")
    private String supplierName;//供应商名称
    @SerializedName("CategoryID")
    private String categoryID;//产品分类ID
    @SerializedName("CategoryName")
    private String categoryName;//产品分类名称
    @SerializedName("UnitPrice")
    private BigDecimal unitPrice;//销售单价
    @SerializedName("DefaultPicture")
    private String defaultPicture;//默认图片（商品在列表时展示的图片）
    @SerializedName("IsOnMainPage")
    private String isOnMainPage;//是否出现在首页的推荐产品中
    @SerializedName("Pictures")
    private ArrayList<String> pictures;//产品详情图片
    @SerializedName("IsOnShelves")
    private String isOnShelves;//是否已上架
    @SerializedName("SalesVolume")
    private int salesVolume;//销售量

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", supplierID='" + supplierID + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", unitPrice=" + unitPrice +
                ", defaultPicture='" + defaultPicture + '\'' +
                ", isOnMainPage='" + isOnMainPage + '\'' +
                ", pictures=" + pictures +
                ", isOnShelves='" + isOnShelves + '\'' +
                '}';
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public String getIsOnMainPage() {
        return isOnMainPage;
    }

    public void setIsOnMainPage(String isOnMainPage) {
        this.isOnMainPage = isOnMainPage;
    }

    public String getIsOnShelves() {
        return isOnShelves;
    }

    public void setIsOnShelves(String isOnShelves) {
        this.isOnShelves = isOnShelves;
    }
}
