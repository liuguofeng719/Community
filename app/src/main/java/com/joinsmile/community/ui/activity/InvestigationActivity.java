package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.InvestigationListResp;
import com.joinsmile.community.bean.InvestigationVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/25.
 * 调查主题列表
 */
public class InvestigationActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private ListViewDataAdapter listViewDataAdapter;
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
        return R.layout.investigation_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.investigation_msg));
        listViewDataAdapter = new ListViewDataAdapter<InvestigationVo>(new ViewHolderCreator<InvestigationVo>() {
            @Override
            public ViewHolderBase<InvestigationVo> createViewHolder(int position) {
                return new ViewHolderBase<InvestigationVo>() {
                    TextView tv_subject;
                    ImageView iv_state;
                    FrameLayout fl_investigation_item;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.investigation_item_activity, null);
                        tv_subject = ButterKnife.findById(view, R.id.tv_subject);
                        iv_state = ButterKnife.findById(view, R.id.iv_state);
                        fl_investigation_item = ButterKnife.findById(view, R.id.fl_investigation_item);
                        return view;
                    }

                    @Override
                    public void showData(int position, InvestigationVo itemData) {
                        tv_subject.setText(itemData.getSubject());
                        if (itemData.getIsFinished() == 0) {
                            iv_state.setImageResource(R.drawable.underway);
                        } else {
                            iv_state.setImageResource(R.drawable.over);
                        }
                        fl_investigation_item.setTag(itemData.getInvestigationID());
                        fl_investigation_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("investigationID", v.getTag().toString());
                                readyGo(InvestigationQuestionsActivity.class, bundle);
                            }
                        });
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getInvestigation();
    }

    //获取调查问卷列表
    private void getInvestigation() {
        showLoading(getString(R.string.common_loading_message));
        Call<InvestigationListResp<List<InvestigationVo>>> investigationListRespCall = getApisNew().getInvestigations(extras.getString("buildingID")).clone();
        investigationListRespCall.enqueue(new Callback<InvestigationListResp<List<InvestigationVo>>>() {
            @Override
            public void onResponse(Call<InvestigationListResp<List<InvestigationVo>>> call,
                                   Response<InvestigationListResp<List<InvestigationVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    InvestigationListResp<List<InvestigationVo>> investigationListResp = response.body();
                    List<InvestigationVo> investigationList = investigationListResp.getInvestigationList();
                    ArrayList dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(investigationList);
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InvestigationListResp<List<InvestigationVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
