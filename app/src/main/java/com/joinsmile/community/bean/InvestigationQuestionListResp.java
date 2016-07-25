package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/25.
 */
public class InvestigationQuestionListResp<T> extends BaseInfoVo {

    @SerializedName("QuestionAswerList")
    private T questionAswerList;

    public T getQuestionAswerList() {
        return questionAswerList;
    }

    public void setQuestionAswerList(T questionAswerList) {
        this.questionAswerList = questionAswerList;
    }
}
