package com.joinsmile.community.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
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
 * 选择门牌号
 */
public class SelectHouseNumberActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.lv_list_view)
    ListView lv_list_view;
    private ListViewDataAdapter<ApartmentNumbersVo> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.select_house_number_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_list_view;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.select_house_number));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ApartmentNumbersVo>() {
            @Override
            public ViewHolderBase<ApartmentNumbersVo> createViewHolder(int position) {
                return new ViewHolderBase<ApartmentNumbersVo>() {

                    TextView textView;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.select_house_number_item_activity, null);
                        textView = ButterKnife.findById(view, R.id.tv_number_name);
                        return view;
                    }

                    @Override
                    public void showData(int position, ApartmentNumbersVo itemData) {
                        textView.setText(itemData.getNumberName());
                        textView.setTag(itemData.getNumberID());
                    }
                };
            }
        });
        lv_list_view.setAdapter(listViewDataAdapter);
        lv_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_number_name);
                Intent intent = new Intent();
                intent.putExtra("numberName", textView.getText().toString());
                intent.putExtra("numberId", textView.getTag().toString());
                setResult(4, intent);
                finish();
            }
        });
        getUserApartments();
    }

    /**
     * 我的小区
     */
    private void getUserApartments() {
        showLoading(getString(R.string.common_loading_message));
        Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
        numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
            @Override
            public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                   Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApartmentNumbersResp<List<ApartmentNumbersVo>> body = response.body();
                    List<ApartmentNumbersVo> apartmentNumberList = body.getApartmentNumberList();
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
}
