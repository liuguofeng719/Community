package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/15.
 */

public class ServiceOrderVo<T> extends BaseInfoVo {
    @SerializedName("OrderInfo")
    private T orderInfo;

    public T getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(T orderInfo) {
        this.orderInfo = orderInfo;
    }

    public static class ServiceOrderInfo{
        @SerializedName("OrderID")
        private String orderId;// 订单ID （此订单ID可以用来在订单支付时或者支付宝和微信支付的签名信息）
        @SerializedName("OrderNumber")
        private String orderNumber;// 订单号
        @SerializedName("OrderTotalPrice")
        private String orderTotalPrice;// 订单总价格

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderTotalPrice() {
            return orderTotalPrice;
        }

        public void setOrderTotalPrice(String orderTotalPrice) {
            this.orderTotalPrice = orderTotalPrice;
        }
    }
}
