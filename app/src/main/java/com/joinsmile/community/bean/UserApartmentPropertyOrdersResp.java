package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/28.
 */
public class UserApartmentPropertyOrdersResp<T> extends BaseInfoVo {

    @SerializedName("PropertyOrderList")
    private T propertyOrderList;

    public T getPropertyOrderList() {
        return propertyOrderList;
    }

    public void setPropertyOrderList(T propertyOrderList) {
        this.propertyOrderList = propertyOrderList;
    }
}
