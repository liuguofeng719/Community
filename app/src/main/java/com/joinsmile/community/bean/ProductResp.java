package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class ProductResp<T> extends BaseInfoVo {

    @SerializedName("Product")
    private T product;

    public T getProduct() {
        return product;
    }

    public void setProduct(T product) {
        this.product = product;
    }
}
