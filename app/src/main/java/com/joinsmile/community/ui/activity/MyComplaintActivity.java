package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.RepairAndComplaintsResp;
import com.joinsmile.community.bean.RepairAndComplaintsVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/7.
 * 我的投诉
 */
public class MyComplaintActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.radio_group)
    RadioGroup rdoGroup;

    private Bundle extras;
    private ListViewDataAdapter<RepairAndComplaintsVo> listViewDataAdapter;

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
        return R.layout.my_complaint_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(extras.getString("title"));
        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_untreated:
                        getRepairAndComplaint(0); //未处理
                        break;
                    case R.id.rdo_dispose:
                        getRepairAndComplaint(1);//已处理
                        break;
                }
            }
        });
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<RepairAndComplaintsVo>() {
            @Override
            public ViewHolderBase<RepairAndComplaintsVo> createViewHolder(int position) {
                return new ViewHolderBase<RepairAndComplaintsVo>() {
                    TextView tv_title;
                    TextView tv_area_name;
                    TextView tv_up_date;
                    TextView tv_detail;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_complaint_item, null);
                        tv_title = ButterKnife.findById(view, R.id.tv_title);
                        tv_area_name = ButterKnife.findById(view, R.id.tv_area_name);
                        tv_up_date = ButterKnife.findById(view, R.id.tv_up_date);
                        tv_detail = ButterKnife.findById(view, R.id.tv_detail);
                        return view;
                    }

                    @Override
                    public void showData(int position, RepairAndComplaintsVo itemData) {
                        tv_title.setText(itemData.getTitle());
                        tv_area_name.setText(itemData.getBuildingName());
                        tv_detail.setTag(itemData.getComplaintID());

                        tv_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("title", extras.getString("title"));
                                bundle.putString("complaintID", v.getTag().toString());
                                readyGo(MyComplaintDetailActivity.class, bundle);
                            }
                        });
                        if (!TextUtils.isEmpty(itemData.getUploadDate())) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                            try {
                                Date parse = simpleDateFormat.parse(itemData.getUploadDate());
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd hh:mm");
                                tv_up_date.setText(simpleDateFormat1.format(parse));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getRepairAndComplaint(extras.getInt("isFinished"));
    }

    //我的投诉
    private void getRepairAndComplaint(int isFinished) {
        showLoading(getString(R.string.common_loading_message));
        Call<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>> listRespCall = getApisNew().getRepairAndComplaints(
                AppPreferences.getString("userId"),
                extras.getInt("isRepair"),
                isFinished).clone();
        listRespCall.enqueue(new Callback<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>>() {
            @Override
            public void onResponse(Call<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>> call,
                                   Response<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    List<RepairAndComplaintsVo> buildingList = response.body().getRepairAndComplaints();
                    ArrayList<RepairAndComplaintsVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(buildingList);
                }
            }

            @Override
            public void onFailure(Call<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
