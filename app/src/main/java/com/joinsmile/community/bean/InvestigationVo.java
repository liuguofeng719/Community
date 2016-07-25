package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/25.
 * 调查问卷
 */
public class InvestigationVo implements Serializable {

    @SerializedName("InvestigationID")
    private String investigationID; //问卷ID
    @SerializedName("Subject")
    private String subject; //问卷主题
    @SerializedName("CreateDate")
    private String createDate; //创建时间
    @SerializedName("QuestionAmount")
    private String questionAmount; //问题数量

    public String getInvestigationID() {
        return investigationID;
    }

    public void setInvestigationID(String investigationID) {
        this.investigationID = investigationID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(String questionAmount) {
        this.questionAmount = questionAmount;
    }
}
