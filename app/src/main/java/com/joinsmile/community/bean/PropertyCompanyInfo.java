package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/21.
 */

public class PropertyCompanyInfo extends BaseInfoVo {

    @SerializedName("PropertyCompanyInfo")
    private Description propertyCompanyInfo;

    public static class Description{

        @SerializedName("DescriptionUrl")
        private String descriptionUrl;

        public String getDescriptionUrl() {
            return descriptionUrl;
        }

        public void setDescriptionUrl(String descriptionUrl) {
            this.descriptionUrl = descriptionUrl;
        }
    }

    public Description getPropertyCompanyInfo() {
        return propertyCompanyInfo;
    }

    public void setPropertyCompanyInfo(Description propertyCompanyInfo) {
        this.propertyCompanyInfo = propertyCompanyInfo;
    }
}
