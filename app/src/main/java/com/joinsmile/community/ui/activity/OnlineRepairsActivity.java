package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.RepairAndComplaintsResp;
import com.joinsmile.community.bean.RepairAndComplaintsVo;
import com.joinsmile.community.netstatus.NetUtils;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.DensityUtils;
import com.joinsmile.community.utils.TLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/11.
 */
public class OnlineRepairsActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_submit)
    TextView tvSubmit;
    @InjectView(R.id.tv_house_number)
    TextView tvHouseNumber;
    @InjectView(R.id.tv_phone)
    TextView tv_phone;
    @InjectView(R.id.edit_question_title)
    EditText edit_question_title;
    @InjectView(R.id.edit_description)
    EditText edit_description;

    @InjectView(R.id.ly_img_container)
    LinearLayout lycontainer;
    @InjectView(R.id.iv_plus)
    FrameLayout iv_plus;
    Bundle extras;
    private List<byte[]> imageBytes = new CopyOnWriteArrayList<>();
    private Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> repairAndComplaintsVoCall;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //拍照和选择本地照片
    @OnClick(R.id.iv_plus)
    public void ivplus() {
        //使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
        Intent intent = new Intent(OnlineRepairsActivity.this, SelectPicPopupWindowActivity.class);
        startActivityForResult(intent, 1);
    }

    //选择报修的房子
    @OnClick(R.id.tv_house_number)
    public void tvHouseNumber() {
        readyGoForResult(SelectHouseNumberActivity.class, 2);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    //提交报修
    @OnClick(R.id.tv_submit)
    public void tvSubmit() {
        //validate
        if (validate()) {
            if (NetUtils.isNetworkAvailable(this)) {
                final Dialog dialog = CommonUtils.showDialog(this);
                dialog.show();
                Map<String, RequestBody> map = new HashMap<>();
                for (byte[] imageByte : imageBytes) {
                    final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageByte);
                    map.put("image\"; filename=\"icon.png", requestBody);
                }
                map.put("userID", CommonUtils.toRequestBody(AppPreferences.getString("userId")));
                map.put("apartmentNumberID", CommonUtils.toRequestBody(tvHouseNumber.getTag().toString()));
                map.put("title", CommonUtils.toRequestBody(edit_question_title.getText().toString()));
                map.put("description", CommonUtils.toRequestBody(edit_description.getText().toString()));
                map.put("linkmanPhoneNumber", CommonUtils.toRequestBody(tv_phone.getText().toString()));
                map.put("isRepair", CommonUtils.toRequestBody("" + extras.getInt("isRepair")));
                repairAndComplaintsVoCall = getApisNew().addRepairAndComplaints(map);
                repairAndComplaintsVoCall.enqueue(new Callback<RepairAndComplaintsResp<RepairAndComplaintsVo>>() {
                    @Override
                    public void onResponse(Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> call,
                                           Response<RepairAndComplaintsResp<RepairAndComplaintsVo>> response) {
                        CommonUtils.dismiss(dialog);
                        TLog.d(TAG_LOG, response.body().toString());
                        if (response.isSuccessful() && response.body().isSuccessfully()) {
                            CommonUtils.make(OnlineRepairsActivity.this, "上报成功");
                            readyGoThenKill(IndexActivity.class);
                        }
                    }

                    @Override
                    public void onFailure(Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> call, Throwable t) {
                        CommonUtils.dismiss(dialog);
                    }
                });

            } else {
                CommonUtils.make(this, getString(R.string.no_network));
            }
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(tvHouseNumber.getTag().toString())) {
            CommonUtils.make(this, getString(R.string.house_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(tv_phone.getText().toString())) {
            CommonUtils.make(this, getString(R.string.phone_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(edit_question_title.getText().toString())) {
            CommonUtils.make(this, getString(R.string.title_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(edit_description.getText().toString())) {
            CommonUtils.make(this, getString(R.string.content_not_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String numberId = intent.getStringExtra("numberId");
        String doneLoction = intent.getStringExtra("doneLoction");
        tvHouseNumber.setText(doneLoction);
        tvHouseNumber.setTag(numberId);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.online_repairs_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(extras.getString("title"));
        tv_phone.setHint(extras.getString("hintPhone"));
    }

    @Override
    protected void onPause() {
        if (repairAndComplaintsVoCall != null) {
            repairAndComplaintsVoCall.clone();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (repairAndComplaintsVoCall != null) {
            repairAndComplaintsVoCall.clone();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                showImage(data);
                break;
            case 4:
                String numberName = data.getStringExtra("numberName");
                String numberId = data.getStringExtra("numberId");
                tvHouseNumber.setText(numberName);
                tvHouseNumber.setTag(numberId);
            default:
                break;
        }
    }

    private void showImage(final Intent data) {
        final ContentResolver contentResolver = this.getContentResolver();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        Bitmap image;
                        try {
                            //这个方法是根据Uri获取Bitmap图片的静态方法
                            image = MediaStore.Images.Media.getBitmap(contentResolver, mImageCaptureUri);
                            OnlineRepairsActivity.this.setImage(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                            Bitmap image = extras.getParcelable("data");
                            setImage(image);
                        }
                    }
                }
            }
        });
    }

    private void setImage(Bitmap image) {
        if (imageBytes.size() == 4) {
            imageBytes.add(CommonUtils.Bitmap2Bytes(image));
            iv_plus.setVisibility(View.GONE);
        } else {
            imageBytes.add(CommonUtils.Bitmap2Bytes(image));
        }
        if (image != null) {
            Bitmap compressImage = CommonUtils.compressImage(image);
            ImageView imageView = new ImageView(OnlineRepairsActivity.this);
            imageView.setImageBitmap(compressImage);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(OnlineRepairsActivity.this, 52), DensityUtils.dip2px(OnlineRepairsActivity.this, 52));
            lp.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(lp);
            lycontainer.addView(imageView);
        }
    }
}
