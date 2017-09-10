package com.topcoder.innovate.util;

import android.app.Activity;

import com.topcoder.innovate.innovate2017.R;
import com.topcoder.innovate.model.Position;
import com.topcoder.innovate.model.Speaker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jie Yao on 2017/8/25.
 * 功能：从网络上获取指定地址中的json数据并解析
 */

public class DataRetriever {
    private List<Position> positionArrayList;
    private String Data;
    private String url;

    public List<Position> retrieveAllPositions(final Activity activity) throws InterruptedException {
        url = activity.getResources().getString(R.string.position);
        //执行okhttp请求数据
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    Data = response.body().string();
                    DataRetriever dataRetriever = new DataRetriever();
                    positionArrayList = dataRetriever.parselocation(Data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();
        return positionArrayList;
    }

    /**
     * 解析speaker数据
     */
    public List<Speaker> parsejson(String data) {
        List<Speaker> speakers = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                Speaker speaker = new Speaker();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                speaker.setDetails(jsonObject1.getString("details"));
                speaker.setName(jsonObject1.getString("name"));
                speaker.setPicture(jsonObject1.getString("picture"));
                speaker.setTitle(jsonObject1.getString("title"));
                JSONArray sessions = jsonObject1.getJSONArray("sessions");
                List<String> sessionsid = new ArrayList<>();
                for (int j = 0; j < sessions.length(); j++) {
                    String session = sessions.getString(j);
                    sessionsid.add(session);
                }
                speaker.setSessionIds(sessionsid);
                speakers.add(speaker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return speakers;
    }

    /**
     * 解析地点数据
     */
    public List<Position> parselocation(String data) {
        List<Position> locationList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0;i < jsonArray.length();i++)
            {
                Position position = new Position();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject object = jsonObject.getJSONObject("fields");
                position.setAddress(object.getString("address"));
                position.setName(object.getString("name"));
                //position.setBlingid(object.getDouble("bing_id"));
                position.setCity(object.getString("city"));
                position.setLatitude(object.getDouble("latitude"));
                position.setLongitude(object.getDouble("longitude"));
                position.setState(object.getString("state"));
                position.setZip(object.getString("zip"));
                locationList.add(position);

            }

        } catch (Exception e) {e.printStackTrace();}
            return locationList;

    }
}


