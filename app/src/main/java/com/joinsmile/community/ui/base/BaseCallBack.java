package com.joinsmile.community.ui.base;

import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/26.
 */
public abstract class BaseCallBack<T> implements Callback<T> {

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                response(call, response);
            }
        });
    }

    @Override
    public void onFailure(final Call<T> call, final Throwable t) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                failure(call, t);
            }
        });
    }

    protected abstract void response(Call<T> call, Response<T> response);

    protected abstract void failure(Call<T> call, Throwable t);
}
