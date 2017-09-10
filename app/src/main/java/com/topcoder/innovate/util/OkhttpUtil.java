package com.topcoder.innovate.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Jie Yao on 2017/9/2.
 */

public class OkhttpUtil {
    private String Data;

    public static void sendOkHttpRequest(final String url, final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
