package com.joinsmile.community.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.UserVoResp;
import com.joinsmile.community.crop.CropFileUtils;
import com.joinsmile.community.crop.PhotoActionHelper;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.Const;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/11.
 */
public class UserInfoActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.circle_face)
    CircleImageView circleFace;
    @InjectView(R.id.tv_nickname)
    EditText tvNickname;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    private Call<UserVoResp> userVoRespCall;
    private String mOutputPath;
    private Dialog dialog;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //更新性别
    @OnClick(R.id.fl_sex)
    public void modifySex() {
        final Dialog dialog = CommonUtils.createDialog(this);
        dialog.setContentView(R.layout.selectsex_activity);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rdo_sex_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                HashMap<String, RequestBody> bodyHashMap = new HashMap<>();
                bodyHashMap.put("userID", CommonUtils.toRequestBody(AppPreferences.getString("userId")));
                String sex = "男";
                switch (checkedId) {
                    case R.id.rdo_sex_man:
                        sex = "男";
                        break;
                    case R.id.rdo_sex_wman:
                        sex = "女";
                        break;
                }
                tvSex.setText(sex);
                bodyHashMap.put("sex", CommonUtils.toRequestBody(sex));
                modifyUserInfo(bodyHashMap);
                CommonUtils.dismiss(dialog);
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 修改用户信息
     *
     * @param bodyHashMap
     */
    private void modifyUserInfo(HashMap<String, RequestBody> bodyHashMap) {
        Call<BaseInfoVo> infoVoCall = getApisNew().modifyUserInfo(bodyHashMap).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TLog.d(TAG_LOG, response.body().isSuccessfully() + "");
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {

            }
        });
    }

    //退出登录
    @OnClick(R.id.tv_submit)
    public void quitLogin() {
        AppPreferences.clearAll();
        finish();
    }

    //修改头像
    @OnClick(R.id.tv_modify_head)
    public void modifyFace() {
        dialog = CommonUtils.createDialog(this);
        dialog.setContentView(R.layout.select_pic_activity);
        RadioGroup radio_group = (RadioGroup) dialog.findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CommonUtils.dismiss(dialog);
                switch (checkedId) {
                    case R.id.tv_take_photo:
//                        choseHeadImageFromCameraCapture();
                        PhotoActionHelper.takePhoto(UserInfoActivity.this).output(mOutputPath).maxOutputWidth(800).requestCode(Const.REQUEST_TAKE_PHOTO).start();
                        break;
                    case R.id.tv_location_photo:
                        try {
                            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, CODE_GALLERY_REQUEST);
                        } catch (ActivityNotFoundException e) {
                        }
                        break;

                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == Const.REQUEST_CLIP_IMAGE || requestCode == Const.REQUEST_TAKE_PHOTO)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                circleFace.setImageBitmap(bitmap);
                HashMap<String, RequestBody> bodyHashMap = new HashMap<>();
                bodyHashMap.put("userID", CommonUtils.toRequestBody(AppPreferences.getString("userId")));
                final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), CommonUtils.Bitmap2Bytes(bitmap));
                bodyHashMap.put("image\"; filename=\"icon.png", requestBody);
                modifyUserInfo(bodyHashMap);
            }
            return;
        } else if (requestCode == CODE_GALLERY_REQUEST) {
            Uri uri = data.getData();
            if (uri == null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                    saveImage(photo, mOutputPath);
                    PhotoActionHelper.clipImage(this).input(mOutputPath).output(mOutputPath).requestCode(Const.REQUEST_CLIP_IMAGE).start();
                }
            } else {
                PhotoActionHelper.clipImage(this).input(CropFileUtils.getPath(this, uri)).output(mOutputPath).requestCode(Const.REQUEST_CLIP_IMAGE).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnTextChanged(value = R.id.tv_nickname, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void modifyNickName(Editable s) {
        TLog.d(TAG_LOG, s.toString());
        HashMap<String, RequestBody> bodyHashMap = new HashMap<>();
        bodyHashMap.put("userID", CommonUtils.toRequestBody(AppPreferences.getString("userId")));
        bodyHashMap.put("nickName", CommonUtils.toRequestBody(s.toString()));
        modifyUserInfo(bodyHashMap);
    }

    //修改密码
    @OnClick(R.id.tv_modify_pwd)
    public void tvModifyPwd() {
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.modify_pwd_text));
        readyGo(RestAndForgotActivity.class, bundle);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.user_info_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.user_info_title));
        mOutputPath = new File(getExternalCacheDir(), "face.jpg").getPath();
        getUserInfo();
    }

    /**
     * 获取个人信息
     */
    private void getUserInfo() {
        userVoRespCall = getApisNew().getUserInfo(AppPreferences.getString("userId"));
        userVoRespCall.enqueue(new Callback<UserVoResp>() {
            @Override
            public void onResponse(Call<UserVoResp> call, Response<UserVoResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserVoResp userVoResp = response.body();
                    UserVoResp.UserInfo userInfo = userVoResp.getUserInfo();
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    builder.showImageForEmptyUri(R.mipmap.logo);
                    builder.showImageOnFail(R.mipmap.logo);
                    builder.showImageOnLoading(R.mipmap.logo);
                    ImageLoader.getInstance().displayImage(userInfo.getHeadPicture(), circleFace, builder.build());
                    tvNickname.setText("" + userInfo.getNickName().trim());
                    tvSex.setText(userInfo.getSex());
                }
            }

            @Override
            public void onFailure(Call<UserVoResp> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (userVoRespCall != null) {
            userVoRespCall.cancel();
        }
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
        super.onDestroy();

    }
}
