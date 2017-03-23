package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseInfoVo implements Serializable {

    @SerializedName("IsSuccessfully")
    private boolean isSuccessfully;
    @SerializedName("ErrorMessage")
    private String errorMessage;

    public boolean isSuccessfully() {
        return isSuccessfully;
    }

    public void setIsSuccessfully(boolean isSuccessfully) {
        this.isSuccessfully = isSuccessfully;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "BaseInfo{" +
                "isSuccessfully=" + isSuccessfully +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
