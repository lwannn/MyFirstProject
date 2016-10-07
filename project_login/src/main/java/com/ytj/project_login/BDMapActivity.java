package com.ytj.project_login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.ytj.project_login.adapter.WithCheckBoxExpandableListAdapter;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.Location;
import com.ytj.project_login.jsonEntity.LocationRoot;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class BDMapActivity extends AppCompatActivity {

    private Context context;
    private MapView mMapView;
    private BaiduMap mBaidumap;
    private ArrayList<HashMap<Integer, Boolean>> isSelected;
    private ArrayList<ArrayList<TelName>> items;
    private String mIp;
    private List<LocationInfo> locationInfos;

    private ArrayList<TelName> selectedItems = new ArrayList<TelName>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MoreMarker();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bdmap);
        context = this;

        initView();
        initData();
    }

    //初始化视图
    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
    }

    //初始化数据
    private void initData() {
        mBaidumap = mMapView.getMap();
        //设置地图的精度，大约200m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaidumap.setMapStatus(msu);

        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");

        Bundle bundle = getIntent().getExtras();
        items = (ArrayList<ArrayList<TelName>>) bundle.getSerializable("item");
        Log.e("System.out", items.get(0).get(0).getName());

        isSelected = WithCheckBoxExpandableListAdapter.isSelected;
        Log.e("System.out", isSelected.get(0).get(0) + "");

        for (int i = 0; i < isSelected.size(); i++) {
            for (int j = 0; j < isSelected.get(i).size(); j++) {
                if (isSelected.get(i).get(j)) {
                    selectedItems.add(items.get(i).get(j));
                }
            }
        }
        Log.e("System.out", selectedItems.toString());

        getLocation();
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
        StringBuilder tels = new StringBuilder();
        tels.append(selectedItems.get(0).getTel());
        for (int i = 1; i < selectedItems.size(); i++) {
            tels.append("," + selectedItems.get(i).getTel());
        }

        String url = "http://" + mIp + "/MapLocal/android/getGps";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("tel", tels.toString())
                .addParams("size", 1 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        LocationRoot locationRoot = gson.fromJson(s, LocationRoot.class);
                        List<List<Location>> data = locationRoot.getData();
                        Log.e("System.out", locationRoot.getRet() + "");

                        locationInfos = new ArrayList<LocationInfo>();
                        for (int i = 0; i < selectedItems.size(); i++) {
                            if (data.get(i).size() != 0) {
                                double latitude = Double.parseDouble(data.get(i).get(0).getLat());
                                double longtitude = Double.parseDouble(data.get(i).get(0).getLon());
                                LocationInfo locationInfo = new LocationInfo(latitude, longtitude, selectedItems.get(i).getName());

                                locationInfos.add(locationInfo);
                            }
                        }

                        mHandler.sendEmptyMessage(0);
                    }
                });
    }

    //有多个标注点的情况下
    private void MoreMarker() {
        mBaidumap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        for (LocationInfo info : locationInfos) {
            //位置
            latLng = new LatLng(info.getLatitude(), info.getLongtitude());
            //图标
            overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(5);
            marker = (Marker) mBaidumap.addOverlay(overlayOptions);

            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }

        //将地图移动到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaidumap.setMapStatus(u);

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
                    }
                });
                //显示InfoWindow
                mBaidumap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

}
