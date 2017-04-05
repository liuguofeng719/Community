package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

public class PicturesVo {

    @SerializedName("ProductID")
    private String productId;

    @SerializedName("PictureUrl")
    private String pictureUrl;

    @SerializedName("NavigateUrl")
    private String navigateUrl;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getNavigateUrl() {
        return navigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }

    @Override
    public String toString() {
        return "PicturesVo{" +
                "pictureUrl='" + pictureUrl + '\'' +
                ", navigateUrl='" + navigateUrl + '\'' +
                '}';
    }
}
