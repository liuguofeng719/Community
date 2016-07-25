package com.joinsmile.community.ui.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/11.
 *
 * @desc 投诉建议
 */
public class ComplaintAdviceActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_header)
    FrameLayout tvHeader;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.iv_history)
    ImageView ivHistory;
    @InjectView(R.id.tv_phone_advice)
    TextView tvPhoneAdvice;
    @InjectView(R.id.tv_online_advice)
    TextView tvOnlineAdvice;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.complaint_advice_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.complaint_suggest));
    }
}
