package com.ytj.project_login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.utils.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class WarnMapActivity extends AppCompatActivity {

    private Context context;
    private MapView mMapView;
    private EditText mEditText;
    private BaiduMap mBaidumap;
    private String mIp;
    private List<LocationInfo> locationInfos;

    private boolean isShowed = true;//是否已经刷新的标志位
    private boolean isFirst = true;
    private boolean isUpdate = true;//是否更新的标志位

//    private ArrayList<TelName> selectedItems;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MoreMarker();
            isShowed = true;
        }
    };

    private PopupWindow mPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bdmap);
        context = this;

        getSupportActionBar().hide();//隐藏actionBar

        initView();
        initData();
        initEvent();
    }

    //初始化视图
    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mEditText = (EditText) findViewById(R.id.et_lonLat);
    }

    //初始化数据
    private void initData() {
        mBaidumap = mMapView.getMap();
        //设置地图的精度，大约200m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaidumap.setMapStatus(msu);

        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
    }

    //初始化事件
    private void initEvent() {
        //不断的进行刷新
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (isUpdate & isShowed) {
                        getLocation();
                        isShowed = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //获取地理位置信息
    private void getLocation() {
        Intent intent = getIntent();
        LocationInfo locationInfo = new LocationInfo(Double.valueOf(intent.getStringExtra("lat")), Double.valueOf(intent.getStringExtra("lon"))
                , 1, intent.getStringExtra("tid"));
        locationInfos = new ArrayList<LocationInfo>();
        locationInfos.add(locationInfo);

        mHandler.sendEmptyMessage(0);


//        String url = "http://" + mIp + "/MapLocal/android/getGps";
//        OkHttpUtils
//                .post()
//                .url(url)
//                .addParams("tel", tels.toString())
//                .addParams("size", 1 + "")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(String s) {
//                        Gson gson = new Gson();
//                        LocationRoot locationRoot = gson.fromJson(s, LocationRoot.class);
//                        List<List<Location>> data = locationRoot.getData();
//                        Log.e("System.out", locationRoot.getRet() + "");
//
//                        locationInfos = new ArrayList<LocationInfo>();
//                        for (int i = 0; i < selectedItems.size(); i++) {
//                            if (data.get(i).size() != 0) {
//                                double latitude = Double.parseDouble(data.get(i).get(0).getLat());
//                                double longtitude = Double.parseDouble(data.get(i).get(0).getLon());
//                                LocationInfo locationInfo = new LocationInfo(latitude, longtitude, selectedItems.get(i).getName());
//
//                                locationInfos.add(locationInfo);
//                            }
//                        }
//
//                        mHandler.sendEmptyMessage(0);
//                    }
//                });
    }



    //有多个标注点的情况下
    private void MoreMarker() {
        mBaidumap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        BitmapDescriptor Objectbitmap = BitmapDescriptorFactory.fromResource(R.drawable.mubiao);
        for (LocationInfo info : locationInfos) {
            //位置
            latLng = new LatLng(info.getLatitude(), info.getLongtitude());
            if (info.getType() == LocationInfo.TEAMUSER) {//如果是组员
                //图标
                overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(5);
            } else {//如果是嫌疑犯
                overlayOptions = new MarkerOptions().position(latLng).icon(Objectbitmap).zIndex(5);
            }
            marker = (Marker) mBaidumap.addOverlay(overlayOptions);

            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }

        if (isFirst) {
            //将地图移动到最后一个经纬度位置(只在第一次绘制时使用)
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mBaidumap.setMapStatus(u);

            isFirst = false;
        }

        //设置对Marker的点击监听事件
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LocationInfo info = (LocationInfo) marker.getExtraInfo().get("info");
                LatLng point = new LatLng(info.getLatitude(), info.getLongtitude());

                InfoWindow mInfoWindow;
                TextView tip = new TextView(getApplicationContext());
                tip.setBackgroundResource(R.drawable.location_tips);
                tip.setPadding(25, 15, 25, 45);
                tip.setText(info.getTips());
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tip);

                mInfoWindow = new InfoWindow(bitmap, point, -57, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        mBaidumap.hideInfoWindow();
                        isUpdate = true;//信息框隐藏的时候就继续更新
                    }
                });
                //显示InfoWindow
                mBaidumap.showInfoWindow(mInfoWindow);
                isUpdate = false;//显示信息框的时候就暂停更新
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back(View view) {
        finish();
    }

    public void searchLocation(View view) {
        isUpdate = false;//暂停更新
//        Toast.makeText(context, mEditText.getText().toString(), Toast.LENGTH_SHORT).show();
        String[] split = mEditText.getText().toString().trim().split(",");
        double lon = Double.parseDouble(split[0]);
        double lat = Double.parseDouble(split[1]);
        LocationInfo locationInfo = new LocationInfo(lat, lon, "");
        locationInfos.add(locationInfo);

        MoreMarker();
    }
}
