package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.InvestigationStatisticsResp;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.widgets.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/24.
 */
public class MyVoteDetailActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListViewForScrollView lvListView;
    private ListViewDataAdapter<InvestigationStatisticsResp.QuestionStatistics> listViewDataAdapter;
    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_vote_detail_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("服务调查");
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<InvestigationStatisticsResp.QuestionStatistics>() {
            @Override
            public ViewHolderBase<InvestigationStatisticsResp.QuestionStatistics> createViewHolder(int position) {
                return new ViewHolderBase<InvestigationStatisticsResp.QuestionStatistics>() {
                    TextView tv_question_title;
                    ListViewForScrollView list_view;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_vote_item_detail_activity, null);
                        tv_question_title = ButterKnife.findById(view, R.id.tv_question_title);
                        list_view = ButterKnife.findById(view, R.id.list_view);
                        return view;
                    }

                    @Override
                    public void showData(int position, InvestigationStatisticsResp.QuestionStatistics itemData) {
                        tv_question_title.setText(position + 1 + "、" + itemData.getQuestionContent());
                        List<InvestigationStatisticsResp.AnswerStatistics> answerStatistics = itemData.getAnswerStatistics();
                        ListViewDataAdapter<InvestigationStatisticsResp.AnswerStatistics>
                                statisticsListViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<InvestigationStatisticsResp.AnswerStatistics>() {
                            @Override
                            public ViewHolderBase<InvestigationStatisticsResp.AnswerStatistics> createViewHolder(int position) {
                                return new ViewHolderBase<InvestigationStatisticsResp.AnswerStatistics>() {
                                    TextView tv_title;
                                    ProgressBar progress_grade;

                                    @Override
                                    public View createView(LayoutInflater layoutInflater) {
                                        View view = layoutInflater.inflate(R.layout.my_vote_item_detail_item_activity, null);
                                        tv_title = ButterKnife.findById(view, R.id.tv_title);
                                        progress_grade = ButterKnife.findById(view, R.id.progress_grade);
                                        return view;
                                    }

                                    @Override
                                    public void showData(int position, InvestigationStatisticsResp.AnswerStatistics itemData) {
                                        tv_title.setText(itemData.getAnswerContent() + " " + itemData.getPercentage() + "%");
                                        //动态修改数据
                                        progress_grade.setProgress(itemData.getPercentage());
                                    }
                                };
                            }
                        });
                        list_view.setAdapter(statisticsListViewDataAdapter);
                        ArrayList<InvestigationStatisticsResp.AnswerStatistics> dataList = statisticsListViewDataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(answerStatistics);
                        statisticsListViewDataAdapter.notifyDataSetChanged();
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getInvestigations();
    }

    public void getInvestigations() {
        showLoading(getString(R.string.common_loading_message));
        Call<InvestigationStatisticsResp> investigationStatisticsRespCall =
                getApisNew().getInvestigationsStatistics(extras.getString("investigationID")).clone();
        investigationStatisticsRespCall.enqueue(new Callback<InvestigationStatisticsResp>() {
            @Override
            public void onResponse(Call<InvestigationStatisticsResp> call,
                                   Response<InvestigationStatisticsResp> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    InvestigationStatisticsResp listResp = response.body();
                    InvestigationStatisticsResp.InvestigationStatistics investigationStatistics = listResp.getInvestigationStatistics();

                    ArrayList<InvestigationStatisticsResp.QuestionStatistics> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(investigationStatistics.getQuestionStatistics());
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InvestigationStatisticsResp> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
