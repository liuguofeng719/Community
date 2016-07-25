package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

public class UserVoResp extends BaseInfoVo {

    @SerializedName("User")
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfo {
        @SerializedName("UserID")
        private String userId;
        @SerializedName("RealName")
        private String realName;
        @SerializedName("NickName")
        private String nickName;
        @SerializedName("HeadPicture")
        private String headPicture;
        @SerializedName("PhoneNumber")
        private String phoneNumber;
        @SerializedName("Sex")
        private String sex;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadPicture() {
            return headPicture;
        }

        public void setHeadPicture(String headPicture) {
            this.headPicture = headPicture;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
