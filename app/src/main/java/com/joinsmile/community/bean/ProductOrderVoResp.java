package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/8/1.
 */
public class ProductOrderVoResp<T> extends BaseInfoVo {

    @SerializedName("ProductOrder")
    private T productOrder;

    public T getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(T productOrder) {
        this.productOrder = productOrder;
    }
}
