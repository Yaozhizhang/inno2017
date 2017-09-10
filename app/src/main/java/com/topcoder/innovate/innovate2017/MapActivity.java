package com.topcoder.innovate.innovate2017;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.topcoder.innovate.model.Position;
import com.topcoder.innovate.util.DataRetriever;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    //要显示地点列表
    private List<Position> positionList;
    // 百度地图控件
    private MapView mMapView = null;
    // 百度地图对象
    private BaiduMap bdMap;
    //定位
    private LocationClient locationClient;
    //监听器
    private MyBDLocationListener bdLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//初始化
        setContentView(R.layout.activity_map);
        //声明locationclient类
        locationClient = new LocationClient(getApplicationContext());
        bdLocationListener = new MyBDLocationListener();/** */
        //注册监听
        locationClient.registerLocationListener(bdLocationListener);

        checkservice();
        //获得地点列表
        try {
            DataRetriever dataRetriever = new DataRetriever();
            positionList = dataRetriever.retrieveAllPositions(MapActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init();//获得mapview
        bdMap = mMapView.getMap();//获取baidumap实例
     //普通地图
        bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        /**设置地图中心点及缩放程度*/
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location);
        LatLng ll =new LatLng(37.783753,-122.401192);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(ll)
                .zoom(18)
                .build();
        // 设置是否显示缩放控件
        mMapView.showZoomControls(true);
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        bdMap.setMapStatus(mMapStatusUpdate);
        bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, marker.getExtraInfo().getString("name")+" , "+marker.getExtraInfo().getString("address"), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        for(Iterator<Position> it = positionList.iterator();it.hasNext();)
        {
            Position temp = it.next();
            LatLng pos = new LatLng(temp.getLatitude(),temp.getLongitude());
            Bundle exteaMsg = new Bundle();
            exteaMsg.putString("name",temp.getName());
            exteaMsg.putString("address",temp.getAddress());
            OverlayOptions options = new MarkerOptions().position(pos).icon(bitmap).extraInfo(exteaMsg);
            bdMap.addOverlay(options);
        }
    }
    private void init() {
        mMapView = (MapView) findViewById(R.id.bmapview);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        //取消监听函数
        if(locationClient != null){
            locationClient.unRegisterLocationListener(bdLocationListener);
        }
        super.onDestroy();
    }


    public void checkservice() {
        //判断权限是否获取
          List<String> permissionList = new ArrayList<>();
         if(ContextCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                 != PackageManager.PERMISSION_GRANTED){
         permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
         }
         if(ContextCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.READ_PHONE_STATE)
                 != PackageManager.PERMISSION_GRANTED){
         permissionList.add(Manifest.permission.READ_PHONE_STATE);
         }
        /** if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED){
         permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
         }*/
         if(!permissionList.isEmpty())
         {
         String[] permissions = permissionList.toArray(new String[permissionList.size()]);
         ActivityCompat.requestPermissions(MapActivity.this,permissions,1);
         }else{
             Log.d("MapActivity","checksever");
             getLocation();
         }
    }
    //当权限未获取时请求权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length > 0)
                {
                    for(int result :grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "定位失败，必须同意所有权限才能使用此功能", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Log.d("MapActivity","onRequestPermissionsResult");
                    getLocation();
                }

                else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    //获取所在位置经纬度及详细地址
    public void getLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        locationClient.start();
        Log.d("MapActivity","getLocation");

    }
    private class MyBDLocationListener implements BDLocationListener {
        public void onReceiveLocation(BDLocation location){
            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = location.getAddrStr();
                Log.d("MapActivity","onReceiveLocation");
                Log.d("my location",latitude+" "+longitude+" "+address);
                if(locationClient.isStarted()){
                    locationClient.stop();
                }
            }
        }
    }


}

