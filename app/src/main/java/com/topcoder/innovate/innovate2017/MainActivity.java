package com.topcoder.innovate.innovate2017;
/** 活动启动模式设置为singletask,避免按返回按钮重复创建mainactvity*/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.topcoder.innovate.model.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String Data;
    private ProgressDialog dialog;
    private String url;
    Boolean aBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        //点击右下角图标打开webviewactivity
        ImageButton infobutton = (ImageButton)findViewById(R.id.image_button_info);
        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
            startActivity(intent);
        }
        });
        //点击speaker图标打开speakerlistactivity
        ImageButton speakerbutton = (ImageButton)findViewById(R.id.image_home_menuspeakers);
        speakerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SpeakerListActivity.class);
                intent.putExtra("Data",Data);
                startActivity(intent);
            }
        });
        //点击map图标打开mapactivity
        ImageButton map = (ImageButton)findViewById(R.id.image_home_menumap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

        /**-------------------------进度条样式-------------------------------------------*/
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setIcon(R.drawable.download);// 设置提示的title的图标
        dialog.setTitle("Loading ...");
        dialog.setMax(100);
        dialog.setProgress(0);//设置当前进度
        dialog.show();

        /**--------------------判断是否联网--------------------------------------------*/
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo.isConnected()) {
            /**-----------------------网络请求---------------------------------------------*/
            aBoolean = true;
        }
        else if(networkInfo.isAvailable())
        {
            dialog.dismiss();
            Toast toast = Toast.makeText(this,"获取数据失败",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else
        {
            dialog.dismiss();
            Toast toast = Toast.makeText(this,"无网络，请检查你的网络连接",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        if(aBoolean)
        {
            url = getResources().getString(R.string.feeds_speakers);
            final ProgressResponseBody.ProgressListener listener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    //计算百分比并更新进度
                    final int percent = (int) (100 * bytesRead / contentLength);
                    if(percent == 100)
                    {
                        dialog.dismiss();
                    }
                    dialog.setProgress(percent);

                }
            };
            //创建一个OkHttpClient，并添加网络拦截器
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            //这里将ResponseBody包装成我们的ProgressResponseBody
                            return response.newBuilder()
                                    .body(new ProgressResponseBody(response.body(), listener))
                                    .build();
                        }
                    }).build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //从响应体读取字符串
                    Data = response.body().string();
                }
            });
        }

    }
}

