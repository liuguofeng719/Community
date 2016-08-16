package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/8/11.
 */
public class ShoppingCartResp<T> extends BaseInfoVo {

    @SerializedName("ShoppingCartProductList")
    private T shoppingCartProductList;
    @SerializedName("TotalPrice")
    private String totalPrice;

    public T getShoppingCartProductList() {
        return shoppingCartProductList;
    }

    public void setShoppingCartProductList(T shoppingCartProductList) {
        this.shoppingCartProductList = shoppingCartProductList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
