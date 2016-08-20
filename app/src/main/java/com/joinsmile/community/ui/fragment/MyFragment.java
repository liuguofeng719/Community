package com.joinsmile.community.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AuthticationVo;
import com.joinsmile.community.ui.activity.AboutActivity;
import com.joinsmile.community.ui.activity.LoginActivity;
import com.joinsmile.community.ui.activity.MyComplaintActivity;
import com.joinsmile.community.ui.activity.MyOrderActivity;
import com.joinsmile.community.ui.activity.MyPropertyPaymentActivity;
import com.joinsmile.community.ui.activity.MyVillageActivity;
import com.joinsmile.community.ui.activity.UserInfoActivity;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.InjectView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {

    @InjectView(R.id.iv_face)
    CircleImageView iv_face;
    @InjectView(R.id.tv_login_title)
    TextView tv_login_title;
    @InjectView(R.id.tv_not_login_title)
    TextView tv_not_login_title;
    @InjectView(R.id.tv_complaint_suggest)
    TextView tv_complaint_suggest;

    //我的小区
    @OnClick(R.id.tv_my_community)
    public void tvMyCommunity() {
        if(checkLogin()) {
            readyGo(MyVillageActivity.class);
        }else{
            readyGo(LoginActivity.class);
        }
    }

    @OnClick(R.id.rl_head_face)
    public void login() {
        if (isLogin()) {
            readyGo(UserInfoActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的投票
    @OnClick(R.id.tv_my_vote)
    public void tvMyVote() {
        
    }

    //我的订单
    @OnClick(R.id.tv_my_order)
    public void tvMyOrder() {
        if (checkLogin()) {
            readyGo(MyOrderActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的报修
    @OnClick(R.id.tv_repairs)
    public void tvRepairs() {
        if (checkLogin()) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "我的报修");
            bundle.putInt("isRepair", 1);
            bundle.putInt("isFinished", 1);
            readyGo(MyComplaintActivity.class, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的投诉
    @OnClick(R.id.tv_complaint_suggest)
    public void tvComplaintSuggest() {
        if (checkLogin()) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "我的投诉");
            bundle.putInt("isRepair", 0);
            bundle.putInt("isFinished", 1);
            readyGo(MyComplaintActivity.class, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的缴费
    @OnClick(R.id.tv_my_payment)
    public void tvMyPayment() {
        if (checkLogin()) {
            readyGo(MyPropertyPaymentActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的收藏
    @OnClick(R.id.tv_my_favorites)
    public void tvMyFavorites() {

    }

    //帮助中心
    @OnClick(R.id.tv_help)
    public void tvHelp() {

    }

    //关于我们
    @OnClick(R.id.tv_about)
    public void tvabout() {
        readyGo(AboutActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

    //检查用户是否登录
    private boolean isLogin() {
        if (checkLogin()) {
            AuthticationVo authticationVo = AppPreferences.getObject(AuthticationVo.class);
            tv_login_title.setVisibility(View.VISIBLE);
            tv_login_title.setText(authticationVo.getPhoneNumber());
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            builder.showImageForEmptyUri(R.mipmap.logo);
            builder.showImageOnFail(R.mipmap.logo);
            builder.showImageOnLoading(R.mipmap.logo);
            builder.cacheInMemory(true);
            builder.cacheOnDisk(true);
            ImageLoader.getInstance().displayImage(authticationVo.getHeadPicture(), iv_face, builder.build());
            tv_not_login_title.setVisibility(View.GONE);
            return true;
        } else {
            tv_login_title.setVisibility(View.GONE);
            tv_not_login_title.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    protected void onFirstUserVisible() {
        TLog.i(TAG_LOG, "onFirstUserVisible");
    }

    @Override
    protected void onUserVisible() {
        TLog.i(TAG_LOG, "onUserVisible");
    }

    @Override
    protected void onUserInvisible() {
        TLog.i(TAG_LOG, "onUserInvisible");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_info;
    }

}
