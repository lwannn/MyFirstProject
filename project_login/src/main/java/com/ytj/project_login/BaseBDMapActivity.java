package com.ytj.project_login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.ytj.project_login.adapter.WithCheckBoxExpandableListAdapterNew;
import com.ytj.project_login.entity.IdTeamName;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.CaseRoot;
import com.ytj.project_login.jsonEntity.CaseSpyteam;
import com.ytj.project_login.jsonEntity.Location;
import com.ytj.project_login.jsonEntity.LocationRoot;
import com.ytj.project_login.jsonEntity.Objects;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class BaseBDMapActivity extends Activity implements WithCheckBoxExpandableListAdapterNew.UpTeamBDMap {

    //是否是personal
    public int IS_PERSONAL = 0;

    {
        IS_PERSONAL = 0;
    }

    //底部两个Button、案件列表和相关人员
    LinearLayout linearLayout;
    TextView case_list;
    TextView case_with_people;
    PopupWindow popupWindow;
    PopupWindow popupWindow_two;
    //用于恢复初始位置
    ImageView init_location;
    LatLng mLatLng = null;

    //预警要用的参数
    Intent intent;
    String lat;
    String lon;
    String wid;
    String tid;
    String radius;

    String m_case_id;
    String m_case_name;
    ArrayList<IdTeamName> idTeamNameList = new ArrayList<IdTeamName>();
    private ArrayList<ArrayList<TelName>> items;//来自selectLocationActivityNew中的items

    private Context context;
    private MapView mMapView;
    private EditText mEditText;
    private BaiduMap mBaidumap;
    private String mIp;
    private List<LocationInfo> locationInfos;
    private TelName selectedItems_item_cop;//selectedItems从PersonalChatActivity中传递过来的第一个TelName

    private boolean isShowed = true;//是否已经刷新的标志位
    private boolean isFirst = true;
    private boolean isUpdate = true;//是否更新的标志位

    private Vector<TelName> selectedItems;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    String s = msg.getData().getString("s");
                    Gson gson = new Gson();
                    LocationRoot locationRoot = gson.fromJson(s, LocationRoot.class);
                    List<List<Location>> data = locationRoot.getData();
                    Log.e("System.out", locationRoot.getRet() + "");

                    locationInfos = new ArrayList<LocationInfo>();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).size() != 0) {
                            double latitude = Double.parseDouble(data.get(i).get(0).getLat());
                            double longtitude = Double.parseDouble(data.get(i).get(0).getLon());
                            LocationInfo locationInfo = null;
                            try {
                                locationInfo = new LocationInfo(latitude, longtitude, selectedItems.get(i).getName());
                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.i("sorry", "指针超出");
                                continue;
                            }
                            locationInfos.add(locationInfo);
                        }
                    }
                    mHandler.sendEmptyMessage(0);

                    break;
                case 0:
                    MoreMarker();
                    isShowed = true;
                    break;
            }
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

//       //给ACtionbar设置返回键，默认的
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();//隐藏actionBar

        initView();
        initData();
        initEvent();
        showWarnMark();
    }

    //初始化视图
    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mEditText = (EditText) findViewById(R.id.et_lonLat);
        linearLayout = (LinearLayout) findViewById(R.id.lay_line);
        case_list = (TextView) findViewById(R.id.case_list);
        case_with_people = (TextView) findViewById(R.id.case_with_people);
        init_location = (ImageView) findViewById(R.id.init_location);
    }

    //初始化数据
    private void initData() {
        mBaidumap = mMapView.getMap();
        //设置地图的精度，大约200m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaidumap.setMapStatus(msu);

        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        if (IS_PERSONAL > 0) {
            selectedItems = getSelectedItems();
            linearLayout.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        wid = intent.getStringExtra("wid");
        tid = intent.getStringExtra("tid");
        radius = intent.getStringExtra("radius");
//         =selectedItems getSelectedItems();
    }

    //初始化事件
    private void initEvent() {

        //baiduMap的长按点击事件
        mBaidumap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Toast.makeText(context, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_SHORT).show();
                View view = View.inflate(context, R.layout.produce_location, null);
                Button button = (Button) view.findViewById(R.id.btn_produceLocation);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                InfoWindow mInfoWindow;
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(button);

                mInfoWindow = new InfoWindow(bitmap, latLng, -57, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        mBaidumap.hideInfoWindow();
                        isUpdate = true;

                        Intent data = new Intent();
                        data.putExtra("latLng", latLng);
                        setResult(1, data);
                        finish();
                    }
                });

                //显示InfoWindow
                mBaidumap.showInfoWindow(mInfoWindow);
                isUpdate = false;
            }
        });
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

        case_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, false);
            }
        });
        case_with_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_two == null) {
                    showPopupWindow_two(v);
                    popupWindow_two.showAsDropDown(v, -v.getHeight() / 3, v.getHeight() / 6);
                } else {
                    popupWindow_two.showAsDropDown(v, -v.getHeight() / 3, v.getHeight() / 6);
                }
            }
        });

        init_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 14);
                //以动画方式更新地图状态，动画耗时 300 ms
                mBaidumap.animateMapStatus(u);
            }
        });
        showPopupWindow(new View(this), true);
    }

    public Vector<TelName> getSelectedItems() {
        return null;
    }

    public List<LocationInfo> getLocationInfos() {
        return null;
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
        //不断的进行刷新
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (selectedItems != null) {
                        if (isUpdate & isShowed) {
                            if (selectedItems.size() != 0) {
                                isShowed = false;
                                getLocation();
                            }
                        }
                    } else {
                        locationInfos = getLocationInfos();
                        if (locationInfos != null) {
                            MoreMarker();
                        }
                    }
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
        Response response_getLocation = null;
        ResponseBody body = null;
        String url = "http://" + mIp + "/MapLocal/android/getGps";
        try {
            response_getLocation = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("tel", tels.toString())
                    .addParams("size", 1 + "")
                    .build()
                    .execute();
            body = response_getLocation.body();
            String s = body.string();

            Message message = Message.obtain();
            message.what = 2;
            Bundle data = new Bundle();
            data.putString("s", s);
            message.setData(data);
            mHandler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (body != null)
                body.close();
            if (response_getLocation != null)
                response_getLocation.close();
        }
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
            mLatLng = latLng;
            isFirst = false;
        }
        showWarnMark();
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

    String[] case_names;

    private boolean showPopupWindow(View view, boolean isPerformClick) {
        // 一个自定义的布局，作为显示的内容
        LinearLayout contentView = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.popupwindow_case_list, null, false);
        String case_id = (String) SharePreferencesUtil.getParam(this, "caseId", "");
        final String case_name = (String) SharePreferencesUtil.getParam(this, "caseName", "");
        final String[] case_ids = case_id.split("::");
        case_names = case_name.split("::");

        //设置popup的界面信息
        for (int i = 0; i < case_ids.length; i++) {
            LinearLayout pop_item = (LinearLayout) LayoutInflater.from(this).inflate(
                    R.layout.popupwindow_case_item, null, false);
            ((TextView) pop_item.getChildAt(0)).setText(case_names[i]);
            ((TextView) pop_item.getChildAt(0)).setTag(i);
            pop_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) ((LinearLayout) v).getChildAt(0).getTag();
                    m_case_id = case_ids[Integer.valueOf(index)];
                    m_case_name = case_names[Integer.valueOf(index)];
                    case_list.setText("案件：" + m_case_name);
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    popupWindow_two = null;
                    getCaseInfo();
                }
            });
            contentView.addView(pop_item);
        }
        //自动触发第一个按钮
        if (isPerformClick) {
            isPerformClick = contentView.getChildAt(0).performClick();
        } else {
            popupWindow = new PopupWindow(contentView,
                    view.getWidth() * 4 / 5, LinearLayout.LayoutParams.WRAP_CONTENT);

            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.i("mengdd", "onTouch : ");

                    return false;
                }
            });
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(view, view.getHeight() / 3, view.getHeight() / 6);
        }
        return isPerformClick;
    }

    //相关人员的popup
    protected void showPopupWindow_two(View v) {
        selectLocationActivityNew content = new selectLocationActivityNew(this);
        View contentView = content.getView();
        content.setCaseInfo(Integer.valueOf(m_case_id), m_case_name, idTeamNameList);
        items = content.getItems();

        popupWindow_two = new PopupWindow(contentView,
                v.getWidth() * 13 / 10, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow_two.setTouchable(true);
        popupWindow_two.setOutsideTouchable(true);
        popupWindow_two.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
            }
        });
        popupWindow_two.setBackgroundDrawable(new BitmapDrawable());
    }


    //获取案件相关信息
    private void getCaseInfo() {
        String url = "http://" + mIp + "/MapLocal/android/getGroup";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("caseid", m_case_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        CaseRoot caseRoot = gson.fromJson(s, CaseRoot.class);
                        idTeamNameList.removeAll(idTeamNameList);
                        final List<Objects> objectsList = caseRoot.getObjects();
                        List<CaseSpyteam> spyteams = caseRoot.getSpyteam();
                        for (int i = 0; i < spyteams.size(); i++) {
                            CaseSpyteam caseSpyteam = spyteams.get(i);
                            int id = caseSpyteam.getId();
                            String name = caseSpyteam.getName();
                            IdTeamName idTeamName = new IdTeamName(id, name);
                            idTeamNameList.add(idTeamName);
                        }
                        int i;
                    }
                });
    }

    private ArrayList<TelName> getSelectedItems_new() {
        return null;
    }

    @Override
    public void addItemAndNotifyMap(int parent, int child_id, boolean is_all) {
        if (selectedItems == null) {
            selectedItems = new Vector<>();
        }
        if (is_all) {
            for (int i = 0; i < items.get(parent).size(); i++) {
                selectedItems.add(items.get(parent).get(i));
            }
        } else {
            selectedItems.add(items.get(parent).get(child_id));
        }
    }

    @Override
    public void removeItemAndNotifyMsp(int parent, int child_id, boolean is_all) {
        if (selectedItems == null) {
            selectedItems = new Vector<TelName>();
        }
        if (is_all) {
            for (int i = 0; i < items.get(parent).size(); i++) {
                selectedItems.remove(items.get(parent).get(i));
            }
        } else {
            selectedItems.remove(items.get(parent).get(child_id));
        }
        if (selectedItems.size() == 0) {
            selectedItems = null;
            mBaidumap.clear();
        }
    }

    //显示预警点
    void showWarnMark() {
        if (wid != null && !wid.equals("")) {
            //定位点坐标
            LatLng ll = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
            mLatLng = ll;
            //设置地图中心点和缩放级别
            float zoom = mBaidumap.getMapStatus().zoom;
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,mBaidumap.getMapStatus().zoom);
            if (isFirst) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 15);
                mBaidumap.setMapStatus(u);
                isFirst = false;
            }
            //画圆，主要是这里
            OverlayOptions ooCircle = new CircleOptions().fillColor(0x38FF4081)
                    .center(ll).stroke(new Stroke(3, 0x78FF4081))
                    .radius(Integer.valueOf(radius));
            mBaidumap.addOverlay(ooCircle);
            //画圆心
            DotOptions oodot = new DotOptions().center(ll).color(0xFFFF005B).radius(20);
            mBaidumap.addOverlay(oodot);
        }
    }
}
