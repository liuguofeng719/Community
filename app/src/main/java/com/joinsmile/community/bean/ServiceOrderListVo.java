package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/18.
 */

public class ServiceOrderListVo<T> extends BaseInfoVo {

    @SerializedName("ServiceOrderList")
    private T serviceOrderList;

    public T getServiceOrderList() {
        return serviceOrderList;
    }

    public void setServiceOrderList(T serviceOrderList) {
        this.serviceOrderList = serviceOrderList;
    }

    public static class ServiceOrder{
        @SerializedName("OrderID")
        private String orderID;//  订单ID
        @SerializedName("ServiceNoteNumber")
        private String serviceNoteNumber;//  服务单号
        @SerializedName("ServiceContent")
        private String serviceContent;//  服务内容
        @SerializedName("TotalPrice")
        private String totalPrice;//  服务金额
        @SerializedName("PaymentDateTime")
        private String paymentDateTime;//  支付时间
        @SerializedName("PaymentWay")
        private String paymentWay;//  支付方式

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getServiceNoteNumber() {
            return serviceNoteNumber;
        }

        public void setServiceNoteNumber(String serviceNoteNumber) {
            this.serviceNoteNumber = serviceNoteNumber;
        }

        public String getServiceContent() {
            return serviceContent;
        }

        public void setServiceContent(String serviceContent) {
            this.serviceContent = serviceContent;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getPaymentDateTime() {
            return paymentDateTime;
        }

        public void setPaymentDateTime(String paymentDateTime) {
            this.paymentDateTime = paymentDateTime;
        }

        public String getPaymentWay() {
            return paymentWay;
        }

        public void setPaymentWay(String paymentWay) {
            this.paymentWay = paymentWay;
        }
    }

    @Override
    public String toString() {
        return "ServiceOrderListVo{" +
                "serviceOrderList=" + serviceOrderList +
                '}';
    }
}
