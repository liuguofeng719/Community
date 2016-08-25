package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/8/25.
 */
public class InvestigationStatisticsResp extends BaseInfoVo {

    @SerializedName("InvestigationStatistics")
    private InvestigationStatistics investigationStatistics;

    public InvestigationStatistics getInvestigationStatistics() {
        return investigationStatistics;
    }

    public void setInvestigationStatistics(InvestigationStatistics investigationStatistics) {
        this.investigationStatistics = investigationStatistics;
    }

    public static class InvestigationStatistics{

        @SerializedName("InvestigationID")
        private String investigationID;
        @SerializedName("InvestigationName")
        private String investigationName;

        @SerializedName("QuestionStatistics")
        private List<QuestionStatistics> questionStatistics;

        public List<QuestionStatistics> getQuestionStatistics() {
            return questionStatistics;
        }

        public void setQuestionStatistics(List<QuestionStatistics> questionStatistics) {
            this.questionStatistics = questionStatistics;
        }

        public String getInvestigationID() {
            return investigationID;
        }

        public void setInvestigationID(String investigationID) {
            this.investigationID = investigationID;
        }

        public String getInvestigationName() {
            return investigationName;
        }

        public void setInvestigationName(String investigationName) {
            this.investigationName = investigationName;
        }
    }

    public static class QuestionStatistics {
        @SerializedName("QuestionID")
        private String questionID;
        @SerializedName("QuestionContent")
        private String questionContent;
        @SerializedName("AnswerStatistics")
        private List<AnswerStatistics> answerStatistics;

        public String getQuestionID() {
            return questionID;
        }

        public void setQuestionID(String questionID) {
            this.questionID = questionID;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public void setQuestionContent(String questionContent) {
            this.questionContent = questionContent;
        }

        public List<AnswerStatistics> getAnswerStatistics() {
            return answerStatistics;
        }

        public void setAnswerStatistics(List<AnswerStatistics> answerStatistics) {
            this.answerStatistics = answerStatistics;
        }
    }

    public static class AnswerStatistics {

        @SerializedName("AnswerID")
        private String answerID;
        @SerializedName("AnswerContent")
        private String answerContent;
        @SerializedName("Percentage")
        private int percentage;

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

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }
}
