package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/18.
 */
public class OrderInfoResp extends BaseInfoVo {

    @SerializedName("OrderInfo")
    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public static class OrderInfo {

        @SerializedName("OrderID")
        private String orderId;//订单ID
        @SerializedName("OrderNumber")
        private String orderNumber; //订单号
        @SerializedName("OrderTotalPrice")
        private String orderTotalPrice;//订单总价格

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
