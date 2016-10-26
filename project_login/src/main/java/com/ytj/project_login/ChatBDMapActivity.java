package com.ytj.project_login;

import android.os.Bundle;

import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2016/10/7.
 */
public class ChatBDMapActivity extends BaseBDMapActivity {
    @Override
    public Vector<TelName> getSelectedItems() {
        return null;
    }

    @Override
    public List<LocationInfo> getLocationInfos() {
        Bundle bundle = getIntent().getExtras();
        ArrayList<LocationInfo> locationInfos = (ArrayList<LocationInfo>) bundle.getSerializable("locationinfolist");
        return locationInfos;
    }
}
