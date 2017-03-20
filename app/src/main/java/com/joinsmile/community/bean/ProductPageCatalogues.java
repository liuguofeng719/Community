package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/20.
 */

public class ProductPageCatalogues<T> extends BaseInfoVo {

    @SerializedName("ProductPageCatalogues")
    private T productPageCatalogues;

    public T getProductPageCatalogues() {
        return productPageCatalogues;
    }

    public void setProductPageCatalogues(T productPageCatalogues) {
        this.productPageCatalogues = productPageCatalogues;
    }

    public static class  ProductPageCatalogue{
        @SerializedName("CatalogueID")
        private String CatalogueID ;//一级分类ID (点击产品展示页一级分类后展示商品API需要传递此参数)
        @SerializedName("CatalogueName")
        private String CatalogueName;// 分类名称
        @SerializedName("CatalogueIcon")
        private String CatalogueIcon;// 分类图标

        public String getCatalogueID() {
            return CatalogueID;
        }

        public void setCatalogueID(String catalogueID) {
            CatalogueID = catalogueID;
        }

        public String getCatalogueName() {
            return CatalogueName;
        }

        public void setCatalogueName(String catalogueName) {
            CatalogueName = catalogueName;
        }

        public String getCatalogueIcon() {
            return CatalogueIcon;
        }

        public void setCatalogueIcon(String catalogueIcon) {
            CatalogueIcon = catalogueIcon;
        }
    }
}
