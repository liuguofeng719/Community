package com.joinsmile.community.utils;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {

    static OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    static final long timeout = 10000 * 5;

    static {
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
//        try {
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom());
//            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
    }

    public static Retrofit getRfHttpsInstance(String baseUrl) {
        Retrofit.Builder rfb = new Retrofit.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long t1 = System.nanoTime();
                Request request = chain.request();
                TLog.d("network", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                TLog.d("network", String.format("Received response for %s in %.1fms%n%s", request.url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        });
        OkHttpClient okHttpClient = builder.build();
        rfb.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient);
        return rfb.build();
    }
}
