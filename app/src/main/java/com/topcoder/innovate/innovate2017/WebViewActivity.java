package com.topcoder.innovate.innovate2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        final WebView web = (WebView)findViewById(R.id.webview);//获取 webview id
        WebSettings settings = web.getSettings(); //设置浏览器属性
        settings.setJavaScriptEnabled(true); //启用JS脚本
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);//启用内置缩放装置
        web.setWebViewClient(new WebViewClient());//新窗口覆盖
        String url = getString(R.string.url);
        web.loadUrl(url);//加载网页
        /**网页后退*/
        web.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()){
                        web.goBack();//后退
                        return true;
                    }
                }
                return false;
            }
        });
        ImageButton homebutton = (ImageButton)findViewById(R.id.image_button_home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent =new Intent(WebViewActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
