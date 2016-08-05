package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AnswerVo;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.InvestigationAnswerVo;
import com.joinsmile.community.bean.InvestigationQuestionListResp;
import com.joinsmile.community.bean.InvestigationQuestionVo;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.ui.base.BaseCallBack;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.DensityUtils;
import com.joinsmile.community.utils.TLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/26.
 */
public class InvestigationQuestionsActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_question_title)
    TextView tvQuestionTitle;
    @InjectView(R.id.tv_done)
    TextView tvDone;
    @InjectView(R.id.ly_questions_content)
    LinearLayout lyQuestionsContent;
    @InjectView(R.id.ly_bottom)
    LinearLayout lyBottom;

    private List<InvestigationQuestionVo> questionAswerList;
    private String investigationID;
    private Dialog dialog;

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
        investigationID = extras.getString("investigationID");
        getInvestigationQuestions(investigationID);
    }

    //以做的题答案
    private Map<String, AnswerVo> answerVoMap = new LinkedHashMap<>();

    //记录当前题索引
    private int currentIndex = 0;
    //当前题什么类型
    private AnswerVo.AnswerType currAnswerType;

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
                    InvestigationQuestionVo iqv = questionAswerList.get(currentIndex);
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
            currAnswerType = AnswerVo.AnswerType.RADIO_TYPE;
            singleChoice(iqv);
        } else if (iqv.getQuestionType() == 2) {//多选
            currAnswerType = AnswerVo.AnswerType.CHECKBOX_TYPE;
            multilChoice(iqv);
        } else if (iqv.getQuestionType() == 3) {//文本
            currAnswerType = AnswerVo.AnswerType.OTHER_TYPE;
            textAction(iqv);
        }
        //初始题目类型
        setAnswerCode(iqv);
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
            radioButton.setTag(iqv.getQuestionID() + "," + answerList.getAnswerID());
            rdg.addView(radioButton);
            //添加下划线
            View view = new View(this);
            view.setBackgroundColor(Color.parseColor("#e5e5e5"));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 1)));
            rdg.addView(view);
        }

        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rdoButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                String answerId = rdoButton.getTag().toString();
                String[] split = answerId.split(",");
                if (answerVoMap.get(split[0]) != null) {
                    AnswerVo answerVo = answerVoMap.get(split[0]);
                    answerVo.setAnswer(split[1]);
                }
            }
        });
        lyQuestionsContent.addView(rdg);

    }

    //设置题库编码
    private void setAnswerCode(InvestigationQuestionVo iqv) {
        if (answerVoMap.get(iqv.getQuestionID()) == null) {
            AnswerVo answerVo = new AnswerVo();
            if (iqv.getQuestionType() == AnswerVo.AnswerType.RADIO_TYPE.getType()) {
                answerVo.setAnswerType(AnswerVo.AnswerType.RADIO_TYPE);
            } else if (iqv.getQuestionType() == AnswerVo.AnswerType.CHECKBOX_TYPE.getType()) {
                answerVo.setAnswerType(AnswerVo.AnswerType.CHECKBOX_TYPE);
            } else if (iqv.getQuestionType() == AnswerVo.AnswerType.OTHER_TYPE.getType()) {
                answerVo.setAnswerType(AnswerVo.AnswerType.OTHER_TYPE);
            }
            answerVoMap.put(iqv.getQuestionID(), answerVo);
        }
    }

    //设置标题
    private void setQuestionTitle(int index, String title) {
        tvQuestionTitle.setText(index + "、" + title);
    }

    final Map<String, String> multipleMap = new HashMap<>();

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
            checkBox.setTag(iqv.getQuestionID() + "," + answerList.getAnswerID());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String answerId = buttonView.getTag().toString();
                    String[] split = answerId.split(",");
                    if (isChecked) {
                        if (!multipleMap.containsKey(split[1])) {
                            multipleMap.put(split[1], split[0]);
                        }
                    } else {
                        if (multipleMap.containsKey(split[1])) {
                            multipleMap.remove(split[1]);
                        }
                    }
                }
            });
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
        lyQuestionsContent.removeAllViews();
        EditText editText = new EditText(this);
        editText.setTag(iqv.getQuestionID());
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(this, 200)));
        editText.setHint("谈谈你的看法");
        editText.setPadding(
                DensityUtils.dip2px(this, 15),
                DensityUtils.dip2px(this, 15),
                DensityUtils.dip2px(this, 15), 0);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AnswerVo answerVo = answerVoMap.get(iqv.getQuestionID());
                if (answerVo != null) {
                    if (s.length() > 0) {
                        answerVo.setAnswer(s.toString());
                    } else {
                        answerVo.setAnswer("");
                    }
                }
            }
        });
        editText.setHintTextColor(Color.parseColor("#999999"));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        editText.setTextColor(Color.parseColor("#333333"));
        editText.setSingleLine(true);
        editText.setMaxLines(8);
        editText.setGravity(Gravity.LEFT | Gravity.TOP);
        lyQuestionsContent.addView(editText);
    }

    //上一个问题
    @OnClick(R.id.tv_prev)
    public void tvPrev() {
        if (currentIndex != 0) {
            currentIndex--;
        } else {
            currentIndex = 0;
        }
        drawAndInitData();
    }

    //初始化和渲染布局
    private void drawAndInitData() {
        InvestigationQuestionVo questionVo = questionAswerList.get(currentIndex);
        //重新渲染布局
        getQuestionType(questionVo);
        //重新初始化控件已选择的值
        initControlValue(questionVo);
    }

    //重新初始化控件已选择的值
    private void initControlValue(InvestigationQuestionVo questionVo) {
        AnswerVo answerVo = answerVoMap.get(questionVo.getQuestionID());
        if (!"".equals(answerVo.getAnswer()) && answerVo.getAnswer() != null) {
            if (currAnswerType.getType() == 1) {// 单选
                RadioGroup childAt = (RadioGroup) lyQuestionsContent.getChildAt(0);
                int childCount = childAt.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (childAt.getChildAt(i) instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) childAt.getChildAt(i);
                        String answerId = radioButton.getTag().toString().split(",")[1];
                        if (answerId.equals(answerVo.getAnswer())) {
                            radioButton.setChecked(true);
                            break;
                        }
                    }
                }
            } else if (currAnswerType.getType() == 2) { //多选
                int childCount = lyQuestionsContent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (lyQuestionsContent.getChildAt(i) instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) lyQuestionsContent.getChildAt(i);
                        String answerId = checkBox.getTag().toString().split(",")[1];
                        String[] answer = answerVo.getAnswer().split(",");
                        for (int j = 0; j < answer.length; j++) {
                            if (answerId.equals(answer[j])) {
                                checkBox.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            } else if (currAnswerType.getType() == 3) {
                TextView childAt = (TextView) lyQuestionsContent.getChildAt(0);
                if (questionVo.getQuestionID().equals(childAt.getTag().toString())) {
                    childAt.setText(answerVo.getAnswer());
                }
            }
        }
    }

    //下一个问题
    @OnClick(R.id.tv_next)
    public void tvNext() {
        if (currAnswerType.getType() == 2) {//多选
            Set<String> keySet = multipleMap.keySet();
            StringBuilder stringBuilder = new StringBuilder();
            String value = "";
            for (String s : keySet) {
                stringBuilder.append(s).append(",");
                value = multipleMap.get(s);
            }
            if (stringBuilder.length() > 0) {
                AnswerVo answerVo = answerVoMap.get(value);
                answerVo.setAnswer(stringBuilder.substring(0, stringBuilder.lastIndexOf(",")));
            }
            multipleMap.clear();
        }
        currentIndex++;
        if (questionAswerList.size() - 1 == currentIndex) {
            lyBottom.setVisibility(View.INVISIBLE);
            tvDone.setVisibility(View.VISIBLE);
        }
        if (questionAswerList.size() > currentIndex) {
            drawAndInitData();
        } else {
            currentIndex = questionAswerList.size() - 1;
            for (String s : answerVoMap.keySet()) {
                TLog.d(TAG_LOG, "key====" + s + " value=" + answerVoMap.get(s).toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
    }

    //提交调查信息
    @OnClick(R.id.tv_done)
    public void tvDone() {

        dialog = CommonUtils.showDialog(this);
        dialog.show();

        InvestigationAnswerVo answerVo = new InvestigationAnswerVo();
        answerVo.setInvestigationID(investigationID);
        answerVo.setUserID(AppPreferences.getString("userId"));

        List<InvestigationAnswerVo.InvestigationAnswer> listAnswer = new ArrayList<>();
        Set<Map.Entry<String, AnswerVo>> entrySet = answerVoMap.entrySet();
        for (Map.Entry<String, AnswerVo> answerVoEntry : entrySet) {
            InvestigationAnswerVo.InvestigationAnswer answer = new InvestigationAnswerVo.InvestigationAnswer();
            String questionID = answerVoEntry.getKey();
            AnswerVo value = answerVoEntry.getValue();
            answer.setQuestionID(questionID);
            answer.setAnswerID(value.getAnswer());
            if (value.getAnswerType() == AnswerVo.AnswerType.OTHER_TYPE) {
                answer.setAnswerContent(value.getAnswer());
            }
            listAnswer.add(answer);
        }

        answerVo.setInvestigationAnswer(listAnswer);
        Call<BaseInfoVo> infoVoCall = getApisNew().uploadUserAnswer(answerVo).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(InvestigationQuestionsActivity.this, getString(R.string.investigation_done));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {
                CommonUtils.dismiss(dialog);
            }
        });
    }
}
