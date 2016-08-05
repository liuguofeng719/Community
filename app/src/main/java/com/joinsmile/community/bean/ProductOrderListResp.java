package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/8/1.
 */
public class ProductOrderListResp<T> extends BaseInfoVo  {

    @SerializedName("ProductOrderList")
    private T productOrderList;

    public T getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(T productOrderList) {
        this.productOrderList = productOrderList;
    }

    @Override
    public String toString() {
        return "ProductOrderListResp{" +
                "productOrderList=" + productOrderList +
                '}';
    }
}
