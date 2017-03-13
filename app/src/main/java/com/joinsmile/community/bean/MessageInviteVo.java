package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

public class MessageInviteVo extends BaseInfoVo {

    @SerializedName("VerifyCode")
    private String verifyCode;
    @SerializedName("UserID")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
