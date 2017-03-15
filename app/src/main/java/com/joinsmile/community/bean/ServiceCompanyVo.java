package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/15.
 */

public class ServiceCompanyVo<T> extends BaseInfoVo {

    @SerializedName("ServiceCompanyList")
    private T serviceCompanyList;

    public T getServiceCompanyList() {
        return serviceCompanyList;
    }

    public void setServiceCompanyList(T serviceCompanyList) {
        this.serviceCompanyList = serviceCompanyList;
    }

    public static class ServiceCompany {
        @SerializedName("CompanyID")
        private String dompanyId;//公司ID（点击公司服务图标后需传递此ID）
        @SerializedName("Description")
        private String description; //公司描述（该处调用将不会返回内容）
        @SerializedName("ServiceContent")
        private String serviceContent;// 服务名称（用于展示在图标下的文字如图中的：“UCC洗衣”）
        @SerializedName("CompanyName")
        private String companyName;// 公司名称
        @SerializedName("CompanyIcon")
        private String companyIcon;// 公司图标（用户展示在界面中）
        @SerializedName("CompanyLogo")
        private String companyLogo;// 公司logo（此处调用不会返回内容）

        public String getDompanyId() {
            return dompanyId;
        }

        public void setDompanyId(String dompanyId) {
            this.dompanyId = dompanyId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getServiceContent() {
            return serviceContent;
        }

        public void setServiceContent(String serviceContent) {
            this.serviceContent = serviceContent;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyIcon() {
            return companyIcon;
        }

        public void setCompanyIcon(String companyIcon) {
            this.companyIcon = companyIcon;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }
    }
}
