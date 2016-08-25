package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/8/25.
 */
public class UserInvestigationListResp  extends BaseInfoVo {

    @SerializedName("UserInvestigationList")
    private List<UserInvestigation> userInvestigationList;

    public List<UserInvestigation> getUserInvestigationList() {
        return userInvestigationList;
    }

    public void setUserInvestigationList(List<UserInvestigation> userInvestigationList) {
        this.userInvestigationList = userInvestigationList;
    }

    public static class UserInvestigation{
        @SerializedName("InvestigationID")
        private String investigationID;
        @SerializedName("InvestigationTitle")
        private String investigationTitle;
        @SerializedName("InvestigationUsers")
        private String investigationUsers;
        @SerializedName("CreateDateTime")
        private String createDateTime;

        public String getInvestigationID() {
            return investigationID;
        }

        public void setInvestigationID(String investigationID) {
            this.investigationID = investigationID;
        }

        public String getInvestigationTitle() {
            return investigationTitle;
        }

        public void setInvestigationTitle(String investigationTitle) {
            this.investigationTitle = investigationTitle;
        }

        public String getInvestigationUsers() {
            return investigationUsers;
        }

        public void setInvestigationUsers(String investigationUsers) {
            this.investigationUsers = investigationUsers;
        }

        public String getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(String createDateTime) {
            this.createDateTime = createDateTime;
        }
    }
}
