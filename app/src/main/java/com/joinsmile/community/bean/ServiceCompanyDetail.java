package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/3/15.
 */

public class ServiceCompanyDetail extends BaseInfoVo {

    @SerializedName("ServiceCompany")
    private ServiceCompanyVo.ServiceCompany serviceCompany;

    public ServiceCompanyVo.ServiceCompany getServiceCompany() {
        return serviceCompany;
    }

    public void setServiceCompany(ServiceCompanyVo.ServiceCompany serviceCompany) {
        this.serviceCompany = serviceCompany;
    }
}
