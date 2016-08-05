package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class ProductListResp<T> extends BaseInfoVo {

    @SerializedName("PageCount")
    private int pageCount;
    @SerializedName("ProductList")
    private T productList;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public T getProductList() {
        return productList;
    }

    public void setProductList(T productList) {
        this.productList = productList;
    }
}
