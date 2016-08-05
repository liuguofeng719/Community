package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/28.
 */
public class UserApartmentPropertyOrdersVo implements Serializable {
    @SerializedName("OrderID")
    private String orderID;// 订单ID
    @SerializedName("OrderNumber")
    private String orderNumber;// 订单号
    @SerializedName("ApartmentFullName")
    private String apartmentFullName;// 房屋全名
    @SerializedName("ApartmentID")
    private String apartmentID;// 房屋ID
    @SerializedName("LayoutID")
    private String layoutID;// 户型ID
    @SerializedName("LayoutName")
    private String layoutName;// 户型名称
    @SerializedName("PropertyCostStandard")
    private String propertyCostStandard;// 户型收费标注
    @SerializedName("PayMonthly")
    private String payMonthly;// 缴纳物业费月份
    @SerializedName("OrderSate")
    private String orderSate;// 订单状态
    @SerializedName("TotalPrice")
    private String totalPrice;// 总金额
    @SerializedName("CreateDate")
    private String createDate;// 订单创建日期
    @SerializedName("PayDate")
    private String payDate;// 订单支付日期
    @SerializedName("PaymentWay")
    private String paymentWay;// 订单支付方式
    @SerializedName("OrderDescription")
    private String orderDescription;// 订单描述（如：3-5月物业费）

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getApartmentFullName() {
        return apartmentFullName;
    }

    public void setApartmentFullName(String apartmentFullName) {
        this.apartmentFullName = apartmentFullName;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public String getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getPropertyCostStandard() {
        return propertyCostStandard;
    }

    public void setPropertyCostStandard(String propertyCostStandard) {
        this.propertyCostStandard = propertyCostStandard;
    }

    public String getPayMonthly() {
        return payMonthly;
    }

    public void setPayMonthly(String payMonthly) {
        this.payMonthly = payMonthly;
    }

    public String getOrderSate() {
        return orderSate;
    }

    public void setOrderSate(String orderSate) {
        this.orderSate = orderSate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }
}
