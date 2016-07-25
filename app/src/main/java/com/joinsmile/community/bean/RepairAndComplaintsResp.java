package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public class RepairAndComplaintsResp<T> extends BaseInfoVo {
    
    @SerializedName("RepairAndComplaints")
    private T repairAndComplaints;

    public T getRepairAndComplaints() {
        return repairAndComplaints;
    }

    public void setRepairAndComplaints(T repairAndComplaints) {
        this.repairAndComplaints = repairAndComplaints;
    }

    @Override
    public String toString() {
        return "RepairAndComplaintsResp{" +
                "repairAndComplaints=" + repairAndComplaints +
                '}';
    }
}

