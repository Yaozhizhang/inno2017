package com.topcoder.innovate.innovate2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class SpeakerListDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_list_detail);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String title = intent.getStringExtra("title");
        String detail = intent.getStringExtra("detail");
        String photo = intent.getStringExtra("photo");
        Log.d("detail",name+"&"+title+"&"+detail+"&"+photo);
        ImageView Photo = (ImageView)findViewById(R.id.photo);
        TextView Name = (TextView)findViewById(R.id.name);
        TextView Title = (TextView)findViewById(R.id.title);
        final TextView Detail = (TextView)findViewById(R.id.detail);
        int photosrc = getResources().getIdentifier(getpicturename(photo),"drawable",getPackageName());
        if(photosrc>0)
        {
            Photo.setImageResource(photosrc);
        }
        Name.setText(name);
        Title.setText(title);
        Detail.setText(detail);



        //返回按钮
        Button back = (Button)findViewById(R.id.detailback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //最下面两个图标功能
        ImageButton homebutton = (ImageButton)findViewById(R.id.image_button_home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent =new Intent(SpeakerListDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton infobutton = (ImageButton)findViewById(R.id.image_button_info);
        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SpeakerListDetailActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getpicturename(String str){
        str.trim();
        String [] temp = str.split("/");
        String name = temp[temp.length-1];
        name = name.split("[.]")[0];
        name = name.toLowerCase(Locale.getDefault());
        if(name.charAt(0)>='0'&&name.charAt(0)<='9')
        {
            name = "x"+name;
        }
        return name;
    }
}
