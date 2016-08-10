package com.joinsmile.community.ui.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liuguofeng719 on 2015/12/15.
 */
public class ViewPageAdpater extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public ViewPageAdpater(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //如果不重写摧毁fragment 加载第三个就会把第二个destroy ，fragment就会重写初始化，导致延迟加载有问题。继承 FragmentPagerAdapter，就不会remove掉第一个
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
