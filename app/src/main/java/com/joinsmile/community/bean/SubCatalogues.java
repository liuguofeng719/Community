package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/14.
 */

public class SubCatalogues<T> extends BaseInfoVo {

    @SerializedName("SubCatalogues")
    private T subCatalogues;

    public T getSubCatalogues() {
        return subCatalogues;
    }

    public void setSubCatalogues(T subCatalogues) {
        this.subCatalogues = subCatalogues;
    }

    public static class SubCatalogue{

        @SerializedName("SubCatalogueID")
        private String subCatalogueId;//品牌或子分类ID
        @SerializedName("PrimaryCatalogueID")
        private String primaryCatalogueId;//一级分类ID
        @SerializedName("SubCatalogueName")
        private String subCatalogueName;//子品牌或子分类名称
        @SerializedName("SubCataloguePicture")
        private String subCataloguePicture;// 子品牌或子分类图片
        @SerializedName("IsBrand")
        private boolean isBrand;// 是否为子品牌

        public String getSubCatalogueId() {
            return subCatalogueId;
        }

        public void setSubCatalogueId(String subCatalogueId) {
            this.subCatalogueId = subCatalogueId;
        }

        public String getPrimaryCatalogueId() {
            return primaryCatalogueId;
        }

        public void setPrimaryCatalogueId(String primaryCatalogueId) {
            this.primaryCatalogueId = primaryCatalogueId;
        }

        public String getSubCatalogueName() {
            return subCatalogueName;
        }

        public void setSubCatalogueName(String subCatalogueName) {
            this.subCatalogueName = subCatalogueName;
        }

        public String getSubCataloguePicture() {
            return subCataloguePicture;
        }

        public void setSubCataloguePicture(String subCataloguePicture) {
            this.subCataloguePicture = subCataloguePicture;
        }

        public boolean isBrand() {
            return isBrand;
        }

        public void setBrand(boolean brand) {
            isBrand = brand;
        }

        @Override
        public String toString() {
            return "SubCatalogue{" +
                    "subCatalogueId='" + subCatalogueId + '\'' +
                    ", primaryCatalogueId='" + primaryCatalogueId + '\'' +
                    ", subCatalogueName='" + subCatalogueName + '\'' +
                    ", subCataloguePicture='" + subCataloguePicture + '\'' +
                    ", isBrand=" + isBrand +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SubCatalogues{" +
                "subCatalogues=" + subCatalogues +
                '}';
    }
}
