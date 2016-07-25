package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class ProductListResp<T> extends BaseInfoVo {

    @SerializedName("ProductList")
    private T productList;

    public T getProductList() {
        return productList;
    }

    public void setProductList(T productList) {
        this.productList = productList;
    }
}
