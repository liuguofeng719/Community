package com.joinsmile.community.ui.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.AnnouncementResp;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
import com.joinsmile.community.bean.BuildingManagementCommittee;
import com.joinsmile.community.bean.OpenDoor;
import com.joinsmile.community.bean.OpenInvitedMember;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.PicturesVoResp;
import com.joinsmile.community.bean.PropertyCompanyInfo;
import com.joinsmile.community.bean.RecommendProductListResp;
import com.joinsmile.community.bean.RecommendProductVo;
import com.joinsmile.community.bean.ServiceCompanyVo;
import com.joinsmile.community.ui.activity.AnnouncementDetailActivity;
import com.joinsmile.community.ui.activity.CatalogueActivity;
import com.joinsmile.community.ui.activity.CourierActivity;
import com.joinsmile.community.ui.activity.InvestigationActivity;
import com.joinsmile.community.ui.activity.LoginActivity;
import com.joinsmile.community.ui.activity.MyVillageActivity;
import com.joinsmile.community.ui.activity.OnlineRepairsActivity;
import com.joinsmile.community.ui.activity.ProductDetailtActivity;
import com.joinsmile.community.ui.activity.ProductListActivity;
import com.joinsmile.community.ui.activity.PropertyMngPaymentActivity;
import com.joinsmile.community.ui.activity.WebViewActivity;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.DensityUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.CircleImageView;
import com.joinsmile.community.widgets.SlideShowView;
import com.joinsmile.community.widgets.VerticalSwitcherTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.com.reformer.rfBleService.BleDevContext;
import cn.com.reformer.rfBleService.BleService;
import cn.com.reformer.rfBleService.OnCompletedListener;
import cn.com.reformer.rfBleService.OnPasswordWriteListener;
import cn.sharesdk.framework.ShareSDK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements SlideShowView.OnImageClickedListener {

    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;
    @InjectView(R.id.tv_location_content)
    TextView tvLocationContent;
    @InjectView(R.id.tv_tips_content)
    VerticalSwitcherTextView tvTipsContent;
    @InjectView(R.id.tv_integral)
    TextView tvIntegral;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Dialog mDialog;
    private Dialog showDialog;
    private Dialog serviceDialog;

    private Call<RecommendProductListResp<List<RecommendProductVo>>> listRespCall;
    private Call<AnnouncementResp> respCall;

    //开门sdk
    private BleService mService;
    private BleService.RfBleKey rfBleKey = null;


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder rawBinder) {
            mService = ((BleService.LocalBinder) rawBinder).getService();
            rfBleKey = mService.getRfBleKey();
            rfBleKey.init(null);
            rfBleKey.setOnCompletedListener(new OnCompletedListener() {
                @Override
                public void OnCompleted(byte[] bytes, int i) {
                    final int result = i;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (result) {
                                case 0:
                                    CommonUtils.make(getActivity(), getString(R.string.result_Success));
                                    break;
                                case 1:
                                    CommonUtils.make(getActivity(), getString(R.string.result_password_error));
                                    break;
                                case 2:
                                    CommonUtils.make(getActivity(), getString(R.string.result_bluetooth_break));
                                    break;
                                case 3:
                                    CommonUtils.make(getActivity(), getString(R.string.result_timeout));
                                    break;
                            }
                        }
                    });
                }
            });

            rfBleKey.setOnPasswordWriteListener(new OnPasswordWriteListener() {
                @Override
                public void OnPasswordWrite(byte[] bytes, int i) {
                    final int result = i;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result == 0) {
                                CommonUtils.make(getActivity(), getString(R.string.result_set_success));
                            } else if (result == 1) {
                                CommonUtils.make(getActivity(), getString(R.string.result_set_failed));
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };
    private ListViewDataAdapter<ServiceCompanyVo.ServiceCompany> lstDataAdapter;

    @OnClick(R.id.tv_intelligence)
    public void tvIntelligence() {
        if (checkLogin()) {
            if (rfBleKey != null) {
                //Scan dev list
                ArrayList<String> list = new ArrayList<>();
                try {
                    ArrayList<BleDevContext> lst = rfBleKey.getDiscoveredDevices();
//                CommonUtils.make(getActivity(), "设备"+lst.toString());
                    if (!lst.isEmpty()) {
                        for (BleDevContext dev : lst) {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(bytePadLeft(Integer.toHexString(dev.mac[0]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[1]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[2]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[3]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[4]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[5]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[6]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[7]), 2))
                                    .append(bytePadLeft(Integer.toHexString(dev.mac[8]), 2))
                                    .append(" (").append(dev.rssi).append(")");
                            //                adapter.add(stringBuffer.toString().toUpperCase());
                            list.add("设备号:" + stringBuffer.toString().toUpperCase());
                        }
                    } else {
                        CommonUtils.make(getActivity(), "没有找到设备");
                        return;
                    }
                } catch (Exception e) {
                    CommonUtils.make(getActivity(), e.getMessage());
                }

//            CommonUtils.make(getActivity(), "===" + list.toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择操作");
                final String[] strings = list.toArray(new String[]{});
                if (strings.length != 0) {
                    builder.setItems(strings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String string = strings[which];
                            int indexOf = string.indexOf(":");
                            final String substring = string.substring(indexOf + 1, string.length()).substring(0, 18);
                            getApisNew().openDoor(AppPreferences.getString("userId"), substring).enqueue(new Callback<OpenDoor>() {
                                @Override
                                public void onResponse(Call<OpenDoor> call, Response<OpenDoor> response) {
                                    OpenDoor body = response.body();
                                    if (response.isSuccessful() && body != null && body.isSuccessfully() && body.isCanBeOpenTheDoor()) {
                                        if (0 == rfBleKey.openDoor(stringToBytes(substring), Integer.decode("5"), "3131313131313131D67D67966DA21300")) {
                                            CommonUtils.make(getActivity(), "开锁成功");
                                        } else {
                                            CommonUtils.make(getActivity(), "开锁失败");
                                        }
                                    } else {
                                        CommonUtils.make(getActivity(), body.getErrorMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Call<OpenDoor> call, Throwable t) {
                                    CommonUtils.make(getActivity(), "网络异常");
                                }
                            });
                        }
                    });
                    builder.create().show();
                } else {
                    CommonUtils.make(getActivity(), "没有找到设备");
                }

//            ActionSheetDialog actionSheetDialog = new ActionSheetDialog(getActivity())
//                    .builder()
//                    .setTitle("请选择操作")
//                    .setCancelable(false)
//                    .setCanceledOnTouchOutside(false);
//            final ArrayList<String> newList = list;
//            for (String s : list) {
//                actionSheetDialog.addSheetItem(s, ActionSheetDialog.SheetItemColor.Blue,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                if (0 == rfBleKey.openDoor(stringToBytes(newList.get(which).substring(0, 18)), Integer.decode("5"), "3131313131313131D67D67966DA21300")) {
//                                    CommonUtils.make(getActivity(), "开锁成功");
//                                }
//                            }
//                        });
//            }
//            actionSheetDialog.show();
            }
        } else {
            readyGo(LoginActivity.class);
        }
//        readyGo(OpenDoorActivity.class);
    }

    public static String bytePadLeft(String str, int len) {
        if (str.length() > 2)
            str = str.substring(str.length() - 2);
        String pad = "0000000000000000";
        return len > str.length() && len <= 16 && len >= 0 ? pad.substring(0, len - str.length()) + str : str;
    }

    public static byte[] stringToBytes(String outStr) {
        if (outStr.length() != 18)
            return null;
        int len = outStr.length() / 2;
        byte[] mac = new byte[len];
        for (int i = 0; i < len; i++) {
            String s = outStr.substring(i * 2, i * 2 + 2);
            if (Integer.valueOf(s, 16) > 0x7F) {
                mac[i] = (byte) (Integer.valueOf(s, 16) - 0xFF - 1);
            } else {
                mac[i] = Byte.valueOf(s, 16);
            }
        }
        return mac;
    }

    @OnClick(R.id.tv_house_keeper)
    public void tvHouseKeeper() {
        mDialog.show();
        final TextView manageService = (TextView) mDialog.findViewById(R.id.tv_manage_service);
        manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.online_repairs));
                    bundle.putInt("isRepair", 1);
                    bundle.putString("hintPhone", "备案人或报修人电话号码");
                    readyGo(OnlineRepairsActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        final TextView complaintSuggest = (TextView) mDialog.findViewById(R.id.tv_complaint_suggest);
        complaintSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.online_advice));
                    bundle.putInt("isRepair", 0);
                    bundle.putString("hintPhone", "投诉人电话号码");
                    readyGo(OnlineRepairsActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        //业委会
        final TextView tvIndustryCouncil = (TextView) mDialog.findViewById(R.id.tv_industry_council);
        tvIndustryCouncil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    if (tvLocationContent.getTag() != null) {
                        String buildId = tvLocationContent.getTag().toString().split(",")[1];
                        Call<BuildingManagementCommittee> managementCommittee = getApisNew().getBuildingManagementCommittee(buildId);
                        managementCommittee.enqueue(new Callback<BuildingManagementCommittee>() {
                            @Override
                            public void onResponse(Call<BuildingManagementCommittee> call, Response<BuildingManagementCommittee> response) {
                                if (response.isSuccessful()) {
                                    BuildingManagementCommittee committee = response.body();
                                    if (committee.isSuccessfully()) {
                                        BuildingManagementCommittee.Description description = committee.getBuildingManagementCommittee();
                                        String descriptionUrl = description.getDescriptionUrl();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title", "业委会");
                                        bundle.putString("navUrl", descriptionUrl);
                                        readyGo(WebViewActivity.class, bundle);
                                    } else {
                                        CommonUtils.make(mContext, committee.getErrorMessage());
                                    }
                                } else {
                                    CommonUtils.make(mContext, response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<BuildingManagementCommittee> call, Throwable t) {

                            }
                        });
                    } else {
                        CommonUtils.make(mContext, "小区不存在");
                    }
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        //物业公司
        final TextView tvPropertyCompany = (TextView) mDialog.findViewById(R.id.tv_property_company);
        tvPropertyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    if (tvLocationContent.getTag() != null) {
                        String buildId = tvLocationContent.getTag().toString().split(",")[1];
                        Call<PropertyCompanyInfo> managementCommittee = getApisNew().getPropertyCompany(buildId);
                        managementCommittee.enqueue(new Callback<PropertyCompanyInfo>() {
                            @Override
                            public void onResponse(Call<PropertyCompanyInfo> call, Response<PropertyCompanyInfo> response) {
                                if (response.isSuccessful()) {
                                    PropertyCompanyInfo committee = response.body();
                                    if (committee.isSuccessfully()) {
                                        PropertyCompanyInfo.Description description = committee.getPropertyCompanyInfo();
                                        String descriptionUrl = description.getDescriptionUrl();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title", "物业公司");
                                        bundle.putString("navUrl", descriptionUrl);
                                        readyGo(WebViewActivity.class, bundle);
                                    } else {
                                        CommonUtils.make(mContext, committee.getErrorMessage());
                                    }
                                } else {
                                    CommonUtils.make(mContext, response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<PropertyCompanyInfo> call, Throwable t) {
                                CommonUtils.make(mContext, t.getMessage());
                            }
                        });
                    } else {
                        CommonUtils.make(mContext, "小区不存在");
                    }
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        final TextView tenementPay = (TextView) mDialog.findViewById(R.id.tv_tenement_pay);
        tenementPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {

                    Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
                    numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
                        @Override
                        public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                               Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall) {
                            if (numbersRespCall.isSuccessful()) {
                                ApartmentNumbersResp<List<ApartmentNumbersVo>> body = numbersRespCall.body();
                                if (body != null) {
                                    String buildingID = "";
                                    List<ApartmentNumbersVo> apartmentNumberList = body.getApartmentNumberList();
                                    int count = apartmentNumberList.size();
                                    for (int i = 0; i < count; i++) {
                                        if (apartmentNumberList.get(i).isDefault() == 1) {
                                            buildingID = apartmentNumberList.get(i).getBuildingID();
                                            break;
                                        }
                                    }

                                    if (count > 0) {

                                        Call<OpenInvitedMember> openInvitedMember = getApisNew().isOpenInvitedMember(buildingID);
                                        openInvitedMember.enqueue(new Callback<OpenInvitedMember>() {

                                            @Override
                                            public void onResponse(Call<OpenInvitedMember> call, Response<OpenInvitedMember> response) {
                                                if (response.isSuccessful()) {
                                                    OpenInvitedMember invitedMember = response.body();
                                                    if (invitedMember.isSuccessfully()) {
                                                        boolean invitedMemberOpen = invitedMember.isOpen();
                                                        if (invitedMemberOpen) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("apartmentNumberID", tvLocationContent.getTag().toString().split(",")[0]);
                                                            bundle.putString("location", tvLocationContent.getTag().toString().split(",")[2]);
                                                            readyGo(PropertyMngPaymentActivity.class, bundle);
                                                        } else {
                                                            CommonUtils.make(mContext, "你在的小区没有缴费权限，请联系物业");
                                                        }
                                                    } else {
                                                        CommonUtils.make(mContext, invitedMember.getErrorMessage());
                                                    }
                                                } else {
                                                    CommonUtils.make(mContext, response.message());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<OpenInvitedMember> call, Throwable t) {
                                                CommonUtils.make(mContext, t.getMessage());
                                            }
                                        });
                                    } else {
                                        CommonUtils.make(getActivity(), "请绑定小区");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {

                        }
                    });

                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });
    }

    //生活服务
    @OnClick(R.id.tv_service)
    public void tvService() {

        serviceQuery();
        serviceDialog.show();

        GridView gridView = (GridView) serviceDialog.findViewById(R.id.grid_service);

        lstDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ServiceCompanyVo.ServiceCompany>() {

            @Override
            public ViewHolderBase<ServiceCompanyVo.ServiceCompany> createViewHolder(int position) {
                final DisplayImageOptions.Builder builder = getBuilder();
                return new ViewHolderBase<ServiceCompanyVo.ServiceCompany>() {

                    ImageView imageView;
                    TextView textView;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View inflate = layoutInflater.inflate(R.layout.service_mng_item, null);
                        imageView = (ImageView) inflate.findViewById(R.id.image);
                        textView = (TextView) inflate.findViewById(R.id.tv_text);
                        return inflate;
                    }

                    @Override
                    public void showData(int position, ServiceCompanyVo.ServiceCompany itemData) {
                        ImageLoader.getInstance().displayImage(itemData.getCompanyIcon(), imageView, builder.build());
                        textView.setText(itemData.getCompanyName());
                        textView.setTag(itemData.getDompanyId());
                    }
                };
            }
        });

        gridView.setAdapter(lstDataAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkLogin()) {
                    TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
                    String tag = tv_text.getTag().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("companyId", tag);
                    bundle.putString("companyName", tv_text.getText().toString());
                    readyGo(CourierActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });
    }

    private void serviceQuery() {
        final Call<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>> companies = getApisNew().getCompanies();
        companies.enqueue(new Callback<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>>() {
            @Override
            public void onResponse(Call<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>> call,
                                   Response<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>> response) {
                if (response.isSuccessful()) {
                    ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>> body = response.body();
                    if (body.isSuccessfully()) {
                        List<ServiceCompanyVo.ServiceCompany> companyList = body.getServiceCompanyList();
                        if (companyList != null && companyList.size() > 0) {
                            ArrayList<ServiceCompanyVo.ServiceCompany> dataList = lstDataAdapter.getDataList();
                            dataList.clear();
                            dataList.addAll(companyList);
                            lstDataAdapter.notifyDataSetChanged();
                        }
                    } else {
                        CommonUtils.make(getActivity(), body.getErrorMessage());
                    }
                } else {
                    CommonUtils.make(getActivity(), response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>> call, Throwable t) {
                CommonUtils.make(getActivity(), t.getMessage());
            }
        });
    }

    //调查问卷
    @OnClick(R.id.tv_vote)
    public void tvVote() {
        if (checkLogin()) {
            Bundle bundle = new Bundle();
            if (tvLocationContent.getTag() != null) {
                bundle.putString("buildingID", tvLocationContent.getTag().toString().split(",")[1]);
            } else {
                bundle.putString("buildingID", "");
            }
            readyGo(InvestigationActivity.class, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //选择小区
    @OnClick(R.id.tv_location_content)
    public void tvLocationContent() {
        if (checkLogin()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("home", true);
            readyGoForResult(MyVillageActivity.class, 1, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            String location = data.getStringExtra("location");
            String buildingID = data.getStringExtra("buildingID");
            String locationDone = data.getStringExtra("locationDone");
            tvLocationContent.setText(location);
            tvLocationContent.setTag(buildingID + "," + locationDone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onFirstUserVisible() {
        TLog.i(TAG_LOG, "onFirstUserVisible");
        common();
    }

    private void common() {
        getUserApartments();
        getPics();
        getOnMainPageProducts();
    }

    //获取轮播图
    private void getPics() {
        Call<PicturesVoResp<List<PicturesVo>>> callPics = getApisNew().getAdvertisementPictures().clone();
        callPics.enqueue(new Callback<PicturesVoResp<List<PicturesVo>>>() {
            @Override
            public void onResponse(Call<PicturesVoResp<List<PicturesVo>>> call, Response<PicturesVoResp<List<PicturesVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        List<PicturesVo> pics = response.body().getPictures();
                        if (pics.size() > 0) {
                            mSlideShowView.setImageUrlList(pics);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PicturesVoResp<List<PicturesVo>>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onUserVisible() {
        TLog.i(TAG_LOG, "onUserVisible");
        common();

    }

    @Override
    protected void onUserInvisible() {
        TLog.i(TAG_LOG, "onUserInvisible");
    }

    @Override
    protected View getLoadingTargetView() {
        return recyclerView;
    }

    @Override
    protected void initViewsAndEvents() {
        ShareSDK.initSDK(getActivity());
        tvTipsContent.setTextStillTime(3000);//设置停留时长间隔
        tvTipsContent.setAnimTime(300);//设置进入和退出的时间间隔
        tvTipsContent.setOnItemClickListener(new VerticalSwitcherTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                AnnouncementResp resp = (AnnouncementResp) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable("announcement", resp);
                readyGo(AnnouncementDetailActivity.class,bundle);
            }
        });
        mDialog = CommonUtils.createDialog(getActivity());
        mDialog.setContentView(R.layout.community_mng_dialog);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        serviceDialog = CommonUtils.createDialog(getActivity());
        serviceDialog.setContentView(R.layout.service_mng_dialog);
        serviceDialog.setCancelable(true);
        serviceDialog.setCanceledOnTouchOutside(true);

        this.mSlideShowView.setOnImageClickedListener(this);
        Intent bindIntent = new Intent(getActivity().getApplicationContext(), BleService.class);
        getActivity().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTipsContent.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        tvTipsContent.stopAutoScroll();
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<RecommendProductVo> data;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

        public HomeAdapter(List<RecommendProductVo> data) {
            this.data = data;
            builder.bitmapConfig(Bitmap.Config.RGB_565);
            builder.cacheInMemory(true);
            builder.cacheOnDisk(true);
            builder.considerExifParams(true);
            builder.showImageForEmptyUri(R.drawable.no_banner);
            builder.showImageOnFail(R.drawable.no_banner);
            builder.showImageOnLoading(R.drawable.no_banner);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_recommend_product, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                RecommendProductVo recommendProductVo = data.get(position);
                ImageLoader.getInstance().displayImage(recommendProductVo.getPicture(), holder.iv_product_img, builder.build());
                holder.iv_product_img.setTag(recommendProductVo.getProductId());
                holder.iv_product_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                holder.tv_product_desc.setTag(recommendProductVo.getProductId());
                holder.tv_product_desc.setText(recommendProductVo.getProductName());
                holder.tv_product_desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                ArrayList<String> headPicture = recommendProductVo.getHeadPicture();
                holder.ly_face.setTag(recommendProductVo.getProductId());
                holder.ly_face.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                holder.ly_face.removeAllViews();
                for (String spic : headPicture) {
                    CircleImageView circleImageView = new CircleImageView(getActivity());
                    circleImageView.setBorderColor(Color.parseColor("#b5e0db"));
                    circleImageView.setBorderWidth(4);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(getActivity(), 48),
                            DensityUtils.dip2px(getActivity(), 48));
                    lp.setMargins(0, 0, 10, 0);
                    circleImageView.setLayoutParams(lp);
                    ImageLoader.getInstance().displayImage(spic, circleImageView, builder.build());
                    holder.ly_face.addView(circleImageView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_product_img;
            TextView tv_product_desc;
            LinearLayout ly_face;

            public MyViewHolder(View view) {
                super(view);
                iv_product_img = ButterKnife.findById(view, R.id.iv_product_img);
                tv_product_desc = ButterKnife.findById(view, R.id.tv_product_desc);
                ly_face = ButterKnife.findById(view, R.id.ly_face);
            }
        }
    }

    /**
     * 我的小区
     */
    private void getUserApartments() {
        if (checkLogin()) {
            showDialog = CommonUtils.showDialog(getActivity(), getString(R.string.common_loading_message));
            showDialog.show();
            Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
            numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
                @Override
                public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                       Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
                    CommonUtils.dismiss(showDialog);
                    if (response.isSuccessful() && response.body() != null) {
                        ApartmentNumbersResp<List<ApartmentNumbersVo>> body = response.body();
                        List<ApartmentNumbersVo> apartmentNumberList = body.getApartmentNumberList();
                        ApartmentNumbersVo numbersVo = null;
                        for (int i = 0; i < apartmentNumberList.size(); i++) {
                            if (apartmentNumberList.get(i).isDefault() == 1) {
                                numbersVo = apartmentNumberList.get(i);
                                String building = numbersVo.getBuilding();
                                String numberId = numbersVo.getNumberID();
                                tvLocationContent.setText(building);
                                tvLocationContent.setTag(numberId + "," + numbersVo.getBuildingID() + "," + building + numbersVo.getUnit() + numbersVo.getApartment());
                                break;
                            }
                        }
                        if (numbersVo != null) {
                            getNewAnnouncement(numbersVo.getBuildingID());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {
                    CommonUtils.dismiss(showDialog);
                }
            });
        } else {
            getNewAnnouncement("");
        }
    }

    //获取公告
    private void getNewAnnouncement(String buildId) {
        respCall = getApisNew().getNewAnnouncement(buildId).clone();
        respCall.enqueue(new Callback<AnnouncementResp>() {
            @Override
            public void onResponse(Call<AnnouncementResp> call, Response<AnnouncementResp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    final AnnouncementResp resp = response.body();
                    final String title = resp.getAnnouncement().getTitle();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<String> strings = Arrays.asList(title);
                            tvTipsContent.setTextList(strings);
                            tvTipsContent.setTag(resp);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AnnouncementResp> call, Throwable t) {

            }
        });
    }

    //获取推荐商品
    private void getOnMainPageProducts() {
        showLoading(getString(R.string.common_loading_message));
        listRespCall = getApisNew().getOnMainPageProducts().clone();
        listRespCall.enqueue(new Callback<RecommendProductListResp<List<RecommendProductVo>>>() {
            @Override
            public void onResponse(Call<RecommendProductListResp<List<RecommendProductVo>>> call,
                                   Response<RecommendProductListResp<List<RecommendProductVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    List<RecommendProductVo> productList = response.body().getProductList();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    layoutManager.setSmoothScrollbarEnabled(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new HomeAdapter(productList));
                }
            }

            @Override
            public void onFailure(Call<RecommendProductListResp<List<RecommendProductVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    //积分，现在测试推荐商品
    @OnClick(R.id.tv_query_all)
    public void tv_query_all() {
        readyGo(ProductListActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.index_main;
    }

    @Override
    public void onImageClicked(int position, SlideShowView.ImageViewTag url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "商品推荐");
        bundle.putString("navUrl", url.getNavUrl());
        readyGo(WebViewActivity.class, bundle);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeDialog();
    }

    private void closeDialog() {
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
        if (showDialog != null) {
            CommonUtils.dismiss(showDialog);
        }
        if (serviceDialog != null) {
            CommonUtils.dismiss(serviceDialog);
        }
    }

    @Override
    public void onDestroy() {
        rfBleKey.free();
        getActivity().unbindService(mServiceConnection);
        if (mSlideShowView != null) {
            mSlideShowView.onDestroy();
        }
        if (listRespCall != null) {
            listRespCall.cancel();
        }
        if (respCall != null) {
            respCall.cancel();
        }
        closeDialog();
        super.onDestroy();
    }
}
