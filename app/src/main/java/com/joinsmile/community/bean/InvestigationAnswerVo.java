package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuguofeng719 on 2016/8/1.
 */
public class InvestigationAnswerVo implements Serializable {
    @SerializedName("InvestigationAnswer")
    List<InvestigationAnswer> investigationAnswer;
    @SerializedName("InvestigationID")
    private String investigationID;
    @SerializedName("UserID")
    private String userID;

    public List<InvestigationAnswer> getInvestigationAnswer() {
        return investigationAnswer;
    }

    public void setInvestigationAnswer(List<InvestigationAnswer> investigationAnswer) {
        this.investigationAnswer = investigationAnswer;
    }

    public String getInvestigationID() {
        return investigationID;
    }

    public void setInvestigationID(String investigationID) {
        this.investigationID = investigationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public static class InvestigationAnswer {
        @SerializedName("QuestionID")
        private String questionID;
        @SerializedName("AnswerID")
        private String answerID;
        @SerializedName("AnswerContent")
        private String answerContent;

        public String getQuestionID() {
            return questionID;
        }

        public void setQuestionID(String questionID) {
            this.questionID = questionID;
        }

        public String getAnswerID() {
            return answerID;
        }

        public void setAnswerID(String answerID) {
            this.answerID = answerID;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }
    }

}
