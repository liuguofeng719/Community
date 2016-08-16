package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/8/1.
 */
public class ProductOrderVo {
    @SerializedName("OrderID")
    private String orderID;//订单ID
    @SerializedName("OrderNumber")
    private String orderNumber;// 订单号
    @SerializedName("OrderSate")
    private String orderSate;//  订单状态 (0：待支付 1：待收货 2：已完成 3：退款中 4：退款完成)
    @SerializedName("OrderStateDescription")
    private String orderStateDescription;
    @SerializedName("TotalPrice")
    private String totalPrice;// 总金额
    @SerializedName("CreateDate")
    private String createDate;// 订单创建日期
    @SerializedName("PayDate")
    private String payDate;// 订单支付日期
    @SerializedName("PaymentWay")
    private String paymentWay;// 订单支付方式
    @SerializedName("RefundPrice")
    private String refundPrice;// 退款金额
    @SerializedName("BuyProductList")
    private List<BuyProductVo> buyProductList;
    @SerializedName("LogisticsCompany")
    private String logisticsCompany;// 物流公司
    @SerializedName("LogisticsNumber")
    private String logisticsNumber; //快递货运单号
    @SerializedName("Remark")
    private String remark; //买家留言
    @SerializedName("BuyersPhoneNumber")
    private String buyersPhoneNumber; //收货人电话
    @SerializedName("BuyersName")
    private String buyersName; //收货人姓名
    @SerializedName("BuyersAddress")
    private String buyersAddress; //收货人地址


    public String getOrderStateDescription() {
        return orderStateDescription;
    }

    public void setOrderStateDescription(String orderStateDescription) {
        this.orderStateDescription = orderStateDescription;
    }
    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuyersPhoneNumber() {
        return buyersPhoneNumber;
    }

    public void setBuyersPhoneNumber(String buyersPhoneNumber) {
        this.buyersPhoneNumber = buyersPhoneNumber;
    }

    public String getBuyersName() {
        return buyersName;
    }

    public void setBuyersName(String buyersName) {
        this.buyersName = buyersName;
    }

    public String getBuyersAddress() {
        return buyersAddress;
    }

    public void setBuyersAddress(String buyersAddress) {
        this.buyersAddress = buyersAddress;
    }

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

    public String getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(String refundPrice) {
        this.refundPrice = refundPrice;
    }

    public List<BuyProductVo> getBuyProductList() {
        return buyProductList;
    }

    public void setBuyProductList(List<BuyProductVo> buyProductList) {
        this.buyProductList = buyProductList;
    }

    public static class BuyProductVo {
        @SerializedName("OrderItemID")
        private String orderItemID;// 购物明细ID
        @SerializedName("OrderID")
        private String orderID ;//订单ID
        @SerializedName("ProductID")
        private String productID ;//产品ID
        @SerializedName("ProductName")
        private String productName ;//产品名称
        @SerializedName("ProductPicture")
        private String productPicture;// 产品图片
        @SerializedName("UnitPrice")
        private String unitPrice ;//单价
        @SerializedName("Amount")
        private int amount ;//购买数量
        @SerializedName("ItemTotalPrice")
        private String itemTotalPrice ;//购买金额
        @SerializedName("HasRefund")
        private int hasRefund ;//是否发生退款（0：未发生 1：发生）

        public String getOrderItemID() {
            return orderItemID;
        }

        public void setOrderItemID(String orderItemID) {
            this.orderItemID = orderItemID;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductPicture() {
            return productPicture;
        }

        public void setProductPicture(String productPicture) {
            this.productPicture = productPicture;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getItemTotalPrice() {
            return itemTotalPrice;
        }

        public void setItemTotalPrice(String itemTotalPrice) {
            this.itemTotalPrice = itemTotalPrice;
        }

        public int getHasRefund() {
            return hasRefund;
        }

        public void setHasRefund(int hasRefund) {
            this.hasRefund = hasRefund;
        }

        @Override
        public String toString() {
            return "BuyProductVo{" +
                    "orderItemID='" + orderItemID + '\'' +
                    ", orderID='" + orderID + '\'' +
                    ", productID='" + productID + '\'' +
                    ", productName='" + productName + '\'' +
                    ", productPicture='" + productPicture + '\'' +
                    ", unitPrice='" + unitPrice + '\'' +
                    ", amount=" + amount +
                    ", itemTotalPrice=" + itemTotalPrice +
                    ", hasRefund=" + hasRefund +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProductOrderVo{" +
                "orderID='" + orderID + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderSate='" + orderSate + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", createDate='" + createDate + '\'' +
                ", payDate='" + payDate + '\'' +
                ", paymentWay='" + paymentWay + '\'' +
                ", refundPrice='" + refundPrice + '\'' +
                ", buyProductList=" + buyProductList +
                '}';
    }
}
