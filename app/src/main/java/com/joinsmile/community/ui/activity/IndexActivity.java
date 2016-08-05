package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.adpater.ViewPageAdpater;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.ui.fragment.HomeFragment;
import com.joinsmile.community.ui.fragment.MyFragment;
import com.joinsmile.community.ui.fragment.ShoppingMallFragment;
import com.joinsmile.community.utils.ExitDoubleClick;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.XViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class IndexActivity extends BaseActivity {

    @InjectView(R.id.viewPager)
    XViewPager viewPager;

    @InjectView(R.id.rdo_menu_group)
    RadioGroup radioGroup;

    private Bundle extras;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (extras != null) {
            int current = extras.getInt("current", 0);
            if (current != 0) {
                extras.remove("current");
                viewPager.setCurrentItem(current, false);
                ((RadioButton) this.radioGroup.getChildAt(current)).setChecked(true);
            }
        }
    }

    @Override
    protected void initViewsAndEvents() {
        viewPager.setEnableScroll(false);
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_ticket:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rdo_order:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rdo_my:
                        viewPager.setCurrentItem(2, false);
                        break;
                }
            }
        });
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ShoppingMallFragment());
        fragmentList.add(new MyFragment());
        ViewPageAdpater viewPageAdpater = new ViewPageAdpater(getSupportFragmentManager(), fragmentList);
        this.viewPager.setAdapter(viewPageAdpater);
        if (extras != null) {
            TLog.d(TAG_LOG, extras.getInt("order") + "");
            this.viewPager.setCurrentItem(extras.getInt("order"));
        } else {
            this.viewPager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDoubleClick.getInstance(this).doDoubleClick(2000, null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
