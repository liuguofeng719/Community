package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.UserInvestigationListResp;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;

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
public class MyVoteActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private ListViewDataAdapter<UserInvestigationListResp.UserInvestigation> listViewDataAdapter;
    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_vote_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("我参与的投票");
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<UserInvestigationListResp.UserInvestigation>() {
            @Override
            public ViewHolderBase<UserInvestigationListResp.UserInvestigation> createViewHolder(int position) {
                return new ViewHolderBase<UserInvestigationListResp.UserInvestigation>() {
                    TextView tv_title;
                    TextView tv_area_name;
                    TextView tv_up_date;
                    TextView tv_detail;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_vote_item_activity, null);
                        tv_title = ButterKnife.findById(view, R.id.tv_title);
                        tv_area_name = ButterKnife.findById(view, R.id.tv_area_name);
                        tv_up_date = ButterKnife.findById(view, R.id.tv_up_date);
                        tv_detail = ButterKnife.findById(view, R.id.tv_detail);
                        return view;
                    }

                    @Override
                    public void showData(int position, UserInvestigationListResp.UserInvestigation itemData) {
                        tv_title.setText(itemData.getInvestigationTitle());
                        tv_area_name.setText("参与调查人数:" + itemData.getInvestigationUsers());
                        tv_detail.setTag(itemData.getInvestigationID());

                        tv_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("investigationID", v.getTag().toString());
                                readyGo(MyVoteDetailActivity.class, bundle);
                            }
                        });
                        tv_up_date.setText("调查日期：" + itemData.getCreateDateTime());
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getInvestigations();
    }

    public void getInvestigations() {
        showLoading(getString(R.string.common_loading_message));
        Call<UserInvestigationListResp> listRespCall = getApisNew().userInvestigations(AppPreferences.getString("userId")).clone();
        listRespCall.enqueue(new Callback<UserInvestigationListResp>() {
            @Override
            public void onResponse(Call<UserInvestigationListResp> call,
                                   Response<UserInvestigationListResp> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    UserInvestigationListResp listResp = response.body();
                    List<UserInvestigationListResp.UserInvestigation> userInvestigationList = listResp.getUserInvestigationList();
                    ArrayList<UserInvestigationListResp.UserInvestigation> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(userInvestigationList);
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserInvestigationListResp> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
