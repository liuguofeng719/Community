package com.joinsmile.community.bean;

/**
 * Created by liuguofeng719 on 2016/7/27.
 */
public class AnswerVo {

    //答案
    private String answer;

    //题类型
    private AnswerType answerType;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }


    public enum AnswerType {

        RADIO_TYPE(1),
        CHECKBOX_TYPE(2),
        OTHER_TYPE(3);

        private int type;

        AnswerType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Override
    public String toString() {
        return "AnswerVo{" +
                " answerType=" + answerType +
                ", answer='" + answer + '\'' +
                '}';
    }
}
