package com.joinsmile.community.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.InvestigationQuestionListResp;
import com.joinsmile.community.bean.InvestigationQuestionVo;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.ui.base.BaseCallBack;
import com.joinsmile.community.utils.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/26.
 */
public class InvestigationQuestionsActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_question_title)
    TextView tvQuestionTitle;
    @InjectView(R.id.ly_questions_content)
    LinearLayout lyQuestionsContent;
    @InjectView(R.id.ly_bottom)
    LinearLayout lyBottom;

    private List<InvestigationQuestionVo> questionAswerList;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    private Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.investigation_questions_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.investigation_app_msg));
        getInvestigationQuestions(extras.getString("investigationID"));
    }

    //以做的题答案
    private Map<String, String> selected = new HashMap<>();
    //记录当前题索引
    private int currentIndex = 0;

    //获取调查的问题和选项
    private void getInvestigationQuestions(final String investigationID) {
        Call<InvestigationQuestionListResp<List<InvestigationQuestionVo>>> questionListRespCall =
                getApisNew().getInvestigationQuestions(investigationID).clone();

        questionListRespCall.enqueue(new BaseCallBack<InvestigationQuestionListResp<List<InvestigationQuestionVo>>>() {

            @Override
            public void response(Call<InvestigationQuestionListResp<List<InvestigationQuestionVo>>> call,
                                 Response<InvestigationQuestionListResp<List<InvestigationQuestionVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    InvestigationQuestionListResp<List<InvestigationQuestionVo>> listResp = response.body();
                    questionAswerList = listResp.getQuestionAswerList();
                    if (questionAswerList != null && questionAswerList.size() > 1) {
                        lyBottom.setVisibility(View.VISIBLE);
                    }
                    //默认加载第一条数据
                    InvestigationQuestionVo iqv = questionAswerList.get(0);
                    getQuestionType(iqv);
                }
            }

            @Override
            public void failure(Call<InvestigationQuestionListResp<List<InvestigationQuestionVo>>> call, Throwable t) {

            }
        });
    }

    //文本类型
    private void getQuestionType(InvestigationQuestionVo iqv) {
        if (iqv.getQuestionType() == 1) {//单选
            singleChoice(iqv);
        } else if (iqv.getQuestionType() == 2) {//多选
            multilChoice(iqv);
        } else if (iqv.getQuestionType() == 3) {//文本
            textAction(iqv);
        }
    }

    //单选
    private void singleChoice(final InvestigationQuestionVo iqv) {
        setQuestionTitle(iqv.getQuestionIndex(), iqv.getQuestion());
        lyQuestionsContent.removeAllViews();

        RadioGroup rdg = new RadioGroup(this);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rdg.setOrientation(RadioGroup.VERTICAL);
        rdg.setLayoutParams(layoutParams);
        rdg.removeAllViews();
        ArrayList<InvestigationQuestionVo.AnswerList> answerLists = iqv.getAnswerLists();
        for (InvestigationQuestionVo.AnswerList answerList : answerLists) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setGravity(Gravity.CENTER_VERTICAL);
            radioButton.setPadding(DensityUtils.dip2px(this, 30), 0, DensityUtils.dip2px(this, 30), 0);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 50)));
            Drawable myImage = getResources().getDrawable(R.drawable.checkbox_style);
            // 这里不能用null，必需采用以下方式设置
            radioButton.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, myImage, null);
            radioButton.setText(answerList.getAnswerContent());
            rdg.addView(radioButton);
            //添加下划线
            View view = new View(this);
            view.setBackgroundColor(Color.parseColor("#e5e5e5"));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 1)));
            rdg.addView(view);
        }
        lyQuestionsContent.addView(rdg);
    }

    //设置标题
    private void setQuestionTitle(int index, String title) {
        tvQuestionTitle.setText(index + "、" + title);
    }

    //多选
    private void multilChoice(final InvestigationQuestionVo iqv) {
        setQuestionTitle(iqv.getQuestionIndex(), iqv.getQuestion());
        lyQuestionsContent.removeAllViews();
        ArrayList<InvestigationQuestionVo.AnswerList> answerLists = iqv.getAnswerLists();
        for (InvestigationQuestionVo.AnswerList answerList : answerLists) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setGravity(Gravity.CENTER_VERTICAL);
            checkBox.setPadding(DensityUtils.dip2px(this, 30), 0, DensityUtils.dip2px(this, 30), 0);
            checkBox.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 50)));
            Drawable myImage = getResources().getDrawable(R.drawable.checkbox_style);
            // 这里不能用null，必需采用以下方式设置
            checkBox.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, myImage, null);
            checkBox.setText(answerList.getAnswerContent());
            lyQuestionsContent.addView(checkBox);

            //添加下划线
            View view = new View(this);
            view.setBackgroundColor(Color.parseColor("#e5e5e5"));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 1)));
            lyQuestionsContent.addView(view);
        }
    }

    //文本处理
    private void textAction(final InvestigationQuestionVo iqv) {
        setQuestionTitle(iqv.getQuestionIndex(), iqv.getQuestion());
    }

    //上一个问题
    @OnClick(R.id.tv_prev)
    public void tvPrev() {
        if (currentIndex != 0) {
            currentIndex--;
        } else {
            currentIndex = 0;
        }
        getQuestionType(questionAswerList.get(currentIndex));
    }

    //下一个问题
    @OnClick(R.id.tv_next)
    public void tvNext() {
        currentIndex++;
        getQuestionType(questionAswerList.get(currentIndex));
    }
}
