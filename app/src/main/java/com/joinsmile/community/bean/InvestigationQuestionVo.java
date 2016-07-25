package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/25.
 */
public class InvestigationQuestionVo implements Serializable {

    @SerializedName("QuestionID")
    private String questionID; //问题ID
    @SerializedName("Question")
    private String question;//问题
    @SerializedName("QuestionType")
    private int questionType;//问题类型 1：单选  2: 多选 3:文本
    @SerializedName("IsMustFill")
    private int isMustFill;// 是否必须填写 0:否 1:是
    @SerializedName("QuestionIndex")
    private int questionIndex;// 问题序号
    @SerializedName("AnswerID")
    private String answerID;// 选项ID
    @SerializedName("AnswerContent")
    private String answerContent;// 选项内容

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getIsMustFill() {
        return isMustFill;
    }

    public void setIsMustFill(int isMustFill) {
        this.isMustFill = isMustFill;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
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
