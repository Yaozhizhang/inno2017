package com.topcoder.innovate.innovate2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.topcoder.innovate.model.Speaker;
import com.topcoder.innovate.model.SpeakerAdapter;
import com.topcoder.innovate.util.DataRetriever;

import java.util.List;

public class SpeakerListActivity extends AppCompatActivity {
    private List<Speaker> speakers;
    private  String Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_list);
        Data =getIntent().getStringExtra("Data");
        DataRetriever dataRetriever = new DataRetriever();
        speakers = dataRetriever.parsejson(Data);
        ListView listview = (ListView) findViewById(R.id.speaker_listview);
        SpeakerAdapter speakerAdapter = new SpeakerAdapter(SpeakerListActivity.this, R.layout.speakerlist_itemview, speakers);
        listview.setAdapter(speakerAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Speaker speaker = speakers.get(position);
                Intent intent = new Intent(SpeakerListActivity.this, SpeakerListDetailActivity.class);
                intent.putExtra("name", speaker.getName());
                intent.putExtra("title", speaker.getTitle());
                intent.putExtra("detail", speaker.getDetails());
                intent.putExtra("photo", speaker.getPicture());
                startActivity(intent);
            }
        });
        //最下面两个图标功能
        ImageButton homebutton = (ImageButton)findViewById(R.id.image_button_home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent =new Intent(SpeakerListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton infobutton = (ImageButton)findViewById(R.id.image_button_info);
        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SpeakerListActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });



    }

}
