package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/8/8.
 */
public class RepairAndComplaintResp<T> extends BaseInfoVo {

    @SerializedName("RepairAndComplaint")
    private T repairAndComplaint;

    public T getRepairAndComplaint() {
        return repairAndComplaint;
    }

    public void setRepairAndComplaint(T repairAndComplaint) {
        this.repairAndComplaint = repairAndComplaint;
    }
}
