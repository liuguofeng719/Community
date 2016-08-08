package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/8/7.
 * 收货地址
 */
public class ReceiveProductAddressResp<T> extends BaseInfoVo {

    @SerializedName("ReceiveProductAddressList")
    public T receiveProductAddressList;

    public T getReceiveProductAddressList() {
        return receiveProductAddressList;
    }

    public void setReceiveProductAddressList(T receiveProductAddressList) {
        this.receiveProductAddressList = receiveProductAddressList;
    }
}
