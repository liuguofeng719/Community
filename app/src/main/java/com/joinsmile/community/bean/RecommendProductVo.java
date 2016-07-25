package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuguofeng719 on 2016/7/20.
 */
public class RecommendProductVo implements Serializable {
    @SerializedName("ProductID")
    private String productId; //商品ID
    @SerializedName("ProductName")
    private String productName; //商品名称
    @SerializedName("Picture")
    private String picture; //产品图片
    @SerializedName("HeadPicture")
    private ArrayList<String> HeadPicture; //头像

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<String> getHeadPicture() {
        return HeadPicture;
    }

    public void setHeadPicture(ArrayList<String> headPicture) {
        HeadPicture = headPicture;
    }
}
