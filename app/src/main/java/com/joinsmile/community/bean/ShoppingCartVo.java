package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/8/11.
 */
public class ShoppingCartVo implements Serializable {

    @SerializedName("ShoppingCartID")
    private String shoppingCartID;//  购物车项ID（删除时需要使用）
    @SerializedName("ProductID")
    private String productID;// 产品ID
    @SerializedName("ProductName")
    private String productName;// 产品名称
    @SerializedName("ProductPicture")
    private String productPicture;//产品图片
    @SerializedName("Amount")
    private String amount;//数量
    @SerializedName("ProductPrice")
    private String productPrice;// 产品价格
    @SerializedName("ItemPirce")
    private String itemPirce;// 行价格
    //默认为true
    private boolean isChecked = true;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getShoppingCartID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(String shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
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

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getItemPirce() {
        return itemPirce;
    }

    public void setItemPirce(String itemPirce) {
        this.itemPirce = itemPirce;
    }

    @Override
    public String toString() {
        return "ShoppingCartVo{" +
                "shoppingCartID='" + shoppingCartID + '\'' +
                ", productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", amount='" + amount + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", itemPirce='" + itemPirce + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
