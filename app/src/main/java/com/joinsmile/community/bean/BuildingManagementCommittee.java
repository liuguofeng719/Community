package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/21.
 */

public class BuildingManagementCommittee extends BaseInfoVo {

    @SerializedName("BuildingManagementCommittee")
    private Description buildingManagementCommittee;

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

    public Description getBuildingManagementCommittee() {
        return buildingManagementCommittee;
    }

    public void setBuildingManagementCommittee(Description buildingManagementCommittee) {
        this.buildingManagementCommittee = buildingManagementCommittee;
    }
}
