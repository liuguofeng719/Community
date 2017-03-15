package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/14.
 * 一级产品分类
 */
public class CataloguesVo<T> extends BaseInfoVo {

    @SerializedName("PrimaryCatalogues")
    private T primaryCatalogues;

    public T getPrimaryCatalogues() {
        return primaryCatalogues;
    }

    public void setPrimaryCatalogues(T primaryCatalogues) {
        this.primaryCatalogues = primaryCatalogues;
    }

    public static class Catalogue{

        @SerializedName("CatalogueID")
        private String catalogueId;
        @SerializedName("CataloguesName")
        private String cataloguesName;

        public String getCatalogueId() {
            return catalogueId;
        }

        public void setCatalogueId(String catalogueId) {
            this.catalogueId = catalogueId;
        }

        public String getCataloguesName() {
            return cataloguesName;
        }

        public void setCataloguesName(String cataloguesName) {
            this.cataloguesName = cataloguesName;
        }

        @Override
        public String toString() {
            return "Catalogue{" +
                    "catalogueId='" + catalogueId + '\'' +
                    ", cataloguesName='" + cataloguesName + '\'' +
                    '}';
        }
    }
}
