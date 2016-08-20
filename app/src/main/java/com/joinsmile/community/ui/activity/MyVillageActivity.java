package com.joinsmile.community.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
import com.joinsmile.community.bean.BaseInfoVo;
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
 * Created by liuguofeng719 on 2016/7/15.
 * 我的小区
 */
public class MyVillageActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.lv_list_view)
    ListView lv_list_view;

    private ListViewDataAdapter<ApartmentNumbersVo> listViewDataAdapter;
    private String numberID;
    private Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall;
    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @OnClick(R.id.tv_join_house)
    public void joinHouse() {
        readyGo(CityActivity.class);
    }

    @OnClick(R.id.tv_invite_member)
    public void tvInviteMember() {
        Bundle bundle = new Bundle();
        bundle.putString("numberID", numberID);
        readyGo(InviteMemberActivity.class, bundle);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_village_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_list_view;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.my_village));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ApartmentNumbersVo>() {
            @Override
            public ViewHolderBase<ApartmentNumbersVo> createViewHolder(int position) {
                return new ViewHolderBase<ApartmentNumbersVo>() {
                    TextView tv_area_name;
                    TextView tv_street;
                    CheckBox checkbox;
                    FrameLayout fl_village;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_village_item_activity, null);
                        tv_area_name = ButterKnife.findById(view, R.id.tv_area_name);
                        tv_street = ButterKnife.findById(view, R.id.tv_street);
                        checkbox = ButterKnife.findById(view, R.id.checkbox);
                        fl_village = ButterKnife.findById(view, R.id.fl_village);
                        return view;
                    }

                    @Override
                    public void showData(int position, ApartmentNumbersVo itemData) {
                        tv_area_name.setText(itemData.getCity() + itemData.getBuilding());
                        tv_street.setText(itemData.getApartment() + itemData.getUnit());
                        if (itemData.isDefault() == 1) {
                            checkbox.setChecked(true);
                            checkbox.setVisibility(View.VISIBLE);
                        } else {
                            checkbox.setChecked(false);
                            checkbox.setVisibility(View.INVISIBLE);
                        }
                        fl_village.setTag(itemData);
                        fl_village.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ApartmentNumbersVo vTag = (ApartmentNumbersVo) v.getTag();
                                setDefaultApartment(vTag);
                            }
                        });
                    }
                };
            }
        });
        lv_list_view.setAdapter(listViewDataAdapter);
        getUserApartments();
    }

    /**
     * 设置默认小区
     *
     * @param numbersVo
     */
    private void setDefaultApartment(final ApartmentNumbersVo numbersVo) {
        Call<BaseInfoVo> infoVoCall = getApisNew().setUserDefaultApartments(AppPreferences.getString("userId"), numbersVo.getNumberID()).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                if (response.isSuccessful() && response.body().isSuccessfully()) {
                    if (extras != null && extras.getBoolean("home")) {
                        Intent intent = new Intent();
                        intent.putExtra("location", numbersVo.getBuilding());
                        intent.putExtra("locationDone", numbersVo.getBuilding() + numbersVo.getUnit() + numbersVo.getApartment());
                        intent.putExtra("buildingID", numbersVo.getNumberID());
                        setResult(10, intent);
                        finish();
                    } else {
                        getUserApartments();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {

            }
        });
    }

    /**
     * 我的小区
     */
    private void getUserApartments() {
        showLoading(getString(R.string.common_loading_message));
        numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
        numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
            @Override
            public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                   Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApartmentNumbersResp<List<ApartmentNumbersVo>> body = response.body();
                    List<ApartmentNumbersVo> apartmentNumberList = body.getApartmentNumberList();
                    for (int i = 0; i < apartmentNumberList.size(); i++) {
                        if (apartmentNumberList.get(i).isDefault() == 1) {
                            numberID = apartmentNumberList.get(i).getNumberID();
                            break;
                        }
                    }
                    ArrayList<ApartmentNumbersVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(apartmentNumberList);
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (numbersRespCall != null) {
            numbersRespCall.cancel();
            numbersRespCall = null;
        }
    }
}
