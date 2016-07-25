package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.joinsmile.community.CommunityApplication;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.CityListResp;
import com.joinsmile.community.bean.CityVo;
import com.joinsmile.community.ui.adpater.CityListAdapter;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.LocationService;
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

public class CityActivity extends BaseActivity implements SiderBar.OnTouchingLetterChangedListener {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.lv_city)
    PinnedHeaderListView lv_city;
    @InjectView(R.id.siderBar)
    SiderBar siderBar;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_location_city)
    TextView tv_location_city;
    @InjectView(R.id.tv_location_city_name)
    TextView tv_location_city_name;

    private Bundle bundleData;
    private CityListAdapter mAdapter;
    private Call<CityListResp<List<CityVo>>> callCityList = null;
    private List<CityVo> cityVoList = new ArrayList<>();
    private LocationService locationService;
    private String provinceName;
    private String cityName;

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
        return R.layout.city_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_city;
    }

    @OnClick(R.id.tv_location_city_name)
    public void locationCityName() {
        if (!TextUtils.isEmpty(cityName)) {
            String cityId = "";
            for (CityVo cityVo : cityVoList) {
                if (cityVo.getCityName().contains(cityName)) {
                    cityId = cityVo.getCityId();
                    break;
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString("cityId", cityId);
            bundle.putString("cityName", tv_location_city_name.getText().toString());
            readyGo(SelectVillageActivity.class, bundle);
        }
    }

    @Override
    protected void initViewsAndEvents() {
        tv_location_city.setText(getString(R.string.location_city));
        tv_header_title.setText(getString(R.string.select_city));
        siderBar.setOnTouchingLetterChangedListener(this);
        //加载所有城市
        this.getCity();
        //开启定位
        new Thread(new Runnable() {
            @Override
            public void run() {
                // -----------location config ------------
                locationService = ((CommunityApplication) (mContext.getApplicationContext())).locationService;
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                locationService.registerListener(mListener);
                locationService.start();
            }
        }).start();


        this.lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
                TLog.d(TAG_LOG, textView.getTag().toString() + " = " + textView.getText());
                Bundle bundle = new Bundle();
                bundle.putString("cityId", textView.getTag().toString());
                bundle.putString("cityName", textView.getText().toString());
                readyGo(SelectVillageActivity.class, bundle);
            }
        });

    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\nProvince : ");
                sb.append(location.getProvince());
                provinceName = location.getProvince();
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                cityName = location.getCity();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_location_city_name.setText(cityName);
                    }
                });
                AppPreferences.putString("provinceName", provinceName);
                AppPreferences.putString("cityName", cityName);
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                TLog.d("baidu", sb.toString());
                locationService.stop();
            }
        }
    };

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        if (callCityList != null) {
            callCityList.cancel();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    //获取到达城市
    private void getCity() {
        showLoading(getString(R.string.common_loading_message));
        callCityList = getApisNew().getCities().clone();
        callCityList.enqueue(new Callback<CityListResp<List<CityVo>>>() {
            @Override
            public void onResponse(Call<CityListResp<List<CityVo>>> call, Response<CityListResp<List<CityVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    final List<CityVo> cityList = response.body().getCityList();
                    setCityData(cityList);
                } else {
                    CommonUtils.make(CityActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CityListResp<List<CityVo>>> call, Throwable t) {
                hideLoading();
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(CityActivity.this, "网络请求超时");
                }
            }
        });
//        callCityList.enqueue(new Callback<CityListResp<List<CityVo>>>() {
//            @Override
//            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {
//                hideLoading();
//                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
//                    final List<CityVo> cityList = response.body().getCityList();
//                    setCityData(cityList);
//                } else {
//                    CommonUtils.make(CityActivity.this, CommonUtils.getCodeToStr(response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                hideLoading();
//                if ("timeout".equals(t.getMessage())) {
//                    CommonUtils.make(CityActivity.this, "网络请求超时");
//                }
//            }
//        });
    }

    class CityComparator implements Comparator<CityVo> {
        //如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
        @Override
        public int compare(CityVo lhs, CityVo rhs) {
            if (lhs.getFirstLatter().equals("#")) {
                return -1;
            } else if (rhs.getFirstLatter().equals("#")) {
                return 1;
            } else {
                return lhs.getFirstLatter().compareTo(rhs.getFirstLatter());
            }
        }
    }

    private void setCityData(List<CityVo> cityList) {
        if (lv_city != null) {
            Collections.sort(cityList, new CityComparator());
            this.cityVoList = cityList;
            mAdapter = new CityListAdapter(this.cityVoList);
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
