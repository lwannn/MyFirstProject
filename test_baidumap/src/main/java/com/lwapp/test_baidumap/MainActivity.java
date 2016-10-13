package com.lwapp.test_baidumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaidumap;
    private List<Info> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
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

        infoList = new ArrayList<Info>();
        Info info = new Info(30.584749, 114.338852, "湖北大学");
        Info info1 = new Info(30.567058, 114.315388, "积玉桥地铁站");
        Info info2 = new Info(30.511879, 114.40486, "光谷广场");
        infoList.add(0, info);
        infoList.add(1, info1);
        infoList.add(2, info2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    //初始化事件
    private void initEvent() {
//        OneMarker();
//        MoreMarker();
        CircleMarker();
    }

    //有多个标注点的情况下
    private void MoreMarker() {
        mBaidumap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        for (Info info : infoList) {
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
                Info info = (Info) marker.getExtraInfo().get("info");
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

    //只有一个Marker的情况下
    private void OneMarker() {
        //定义Maker坐标点
        LatLng point = new LatLng(30.584749, 114.338852);
        //构建maker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        //构建MakerOptions,用于在地图上添加Maker(MarkerOptions继承OverlayOptions)
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Maker,并显示
        mBaidumap.addOverlay(option);

        //将中心点定到设置的经纬度
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(point);
        mBaidumap.animateMapStatus(msu);

        //弹出窗覆盖物
        //创建InfoWindow展示的View
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.location_tips);
        button.setText("湖北大学你值得拥有！");
        button.setPadding(30, 15, 30, 45);
        button.setAlpha(0.05f);
        BitmapDescriptor b = BitmapDescriptorFactory.fromView(button);
        //创建InfoWindow,传入View,地理坐标，y轴偏移量
//        final InfoWindow mInfoWindow = new InfoWindow(button, point, -53);
        final InfoWindow mInfoWindow = new InfoWindow(b, point, -53, new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                //隐藏InfoWindow
                mBaidumap.hideInfoWindow();
            }
        });

        //设置对marker的点击事件
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //显示InfoWindow
                mBaidumap.showInfoWindow(mInfoWindow);
                return true;
            }
        });

//        //点击地图，隐藏InfoWindow
//        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                mBaidumap.hideInfoWindow();
//            }
//
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//                return false;
//            }
//        });
    }

    //圆形的几何覆盖物
    private void CircleMarker() {
        //定义圆心点
        LatLng point = new LatLng(30.584749, 114.338852);
        //构建用户绘制圆形(半径为100m的圆)的options对象
        OverlayOptions options = new CircleOptions()
                .center(point)
                .radius(100)
                .stroke(new Stroke(3, 0x99C82323))
                .fillColor(0xffffff);
        //在地图上添加options,用于显示
        mBaidumap.addOverlay(options);

        //将中心点定到设置的经纬度
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(point);
        mBaidumap.animateMapStatus(msu);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_map_common://普通地图
                mBaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.id_map_site://卫星地图
                mBaidumap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
