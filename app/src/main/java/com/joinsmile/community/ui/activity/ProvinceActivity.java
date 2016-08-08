package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ProvinceListResp;
import com.joinsmile.community.bean.ProvinceVo;
import com.joinsmile.community.ui.adpater.ProvinceListAdapter;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.PinnedHeaderListView;
import com.joinsmile.community.widgets.SiderBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvinceActivity extends BaseActivity implements SiderBar.OnTouchingLetterChangedListener {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.lv_city)
    PinnedHeaderListView lv_city;
    @InjectView(R.id.siderBar)
    SiderBar siderBar;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    private Bundle bundleData;
    private ProvinceListAdapter mAdapter;
    private Call<ProvinceListResp<List<ProvinceVo>>> provinceListRespCall = null;
    private List<ProvinceVo> cityVoList = new ArrayList<>();


    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.province_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_city;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.select_province));
        siderBar.setOnTouchingLetterChangedListener(this);
        //加载所有城市
        this.getCity();
        this.lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
                TLog.d(TAG_LOG, textView.getTag().toString() + " = " + textView.getText());
                Bundle bundle = new Bundle();
                bundle.putString("provinceId", textView.getTag().toString());
                bundle.putString("provinceName", textView.getText().toString());
                readyGo(CityAddressActivity.class, bundle);
            }
        });
    }

    @Override
    public void onStop() {
        if (provinceListRespCall != null) {
            provinceListRespCall.cancel();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //获取到达城市
    private void getCity() {
        showLoading(getString(R.string.common_loading_message));
        provinceListRespCall = getApisNew().getAllProvinces().clone();
        provinceListRespCall.enqueue(new Callback<ProvinceListResp<List<ProvinceVo>>>() {
            @Override
            public void onResponse(Call<ProvinceListResp<List<ProvinceVo>>> call,
                                   Response<ProvinceListResp<List<ProvinceVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    final List<ProvinceVo> cityList = response.body().getProvinceList();
                    setCityData(cityList);
                } else {
                    CommonUtils.make(ProvinceActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ProvinceListResp<List<ProvinceVo>>> call, Throwable t) {
                hideLoading();
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(ProvinceActivity.this, "网络请求超时");
                }
            }
        });
    }

    class CityComparator implements Comparator<ProvinceVo> {
        //如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
        @Override
        public int compare(ProvinceVo lhs, ProvinceVo rhs) {
            if (lhs.getFirstLatter().equals("#")) {
                return -1;
            } else if (rhs.getFirstLatter().equals("#")) {
                return 1;
            } else {
                return lhs.getFirstLatter().compareTo(rhs.getFirstLatter());
            }
        }
    }

    private void setCityData(List<ProvinceVo> cityList) {
        if (lv_city != null) {
            Collections.sort(cityList, new CityComparator());
            this.cityVoList = cityList;
            mAdapter = new ProvinceListAdapter(this.cityVoList);
            lv_city.setAdapter(mAdapter);
            lv_city.setOnScrollListener(mAdapter);
            lv_city.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.header_item, lv_city, false));
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        if (!TextUtils.isEmpty(s)) {
            lv_city.setSelection(this.mAdapter.getPositionForSection(findIndex(s)));
        }
    }

    //根据s找到对应的s的位置
    public int findIndex(String s) {
        for (int i = 0; i < SiderBar.sideBar.length; i++) {
            //根据city中的sortKey进行比较
            if (s.equals(SiderBar.sideBar[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
