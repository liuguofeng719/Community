package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.widgets.SlideShowView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/8/8.
 * 我的投诉，我的建议
 */
public class MyComplaintDetailActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.slideShowView)
    SlideShowView slideShowView;
    @InjectView(R.id.tv_brief_desc)
    TextView tvBriefDesc;
    @InjectView(R.id.tv_link_phone)
    TextView tvLinkPhone;
    @InjectView(R.id.tv_up_date)
    TextView tvUpDate;
    @InjectView(R.id.tv_address_detail)
    TextView tvAddressDetail;

    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void btnBack(){
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_complaint_detail_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(extras.getString("title"));
    }
}
