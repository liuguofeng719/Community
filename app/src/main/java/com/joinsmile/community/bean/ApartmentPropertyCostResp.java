package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/28.
 * 物业费收费标准/月
 */
public class ApartmentPropertyCostResp extends BaseInfoVo {

    @SerializedName("PropertyCostStandard")
    private String propertyCostStandard;

    public String getPropertyCostStandard() {
        return propertyCostStandard;
    }

    public void setPropertyCostStandard(String propertyCostStandard) {
        this.propertyCostStandard = propertyCostStandard;
    }

    @Override
    public String toString() {
        return "ApartmentPropertyCostResp{" +
                "propertyCostStandard='" + propertyCostStandard + '\'' +
                '}';
    }
}
