package com.ytj.project_login;

import android.os.Bundle;

import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时地图
 * Created by Administrator on 2016/10/10.
 */
public class ChatTrueTimeBDMapActivity extends BaseBDMapActivity {
    @Override
    public ArrayList<TelName> getSelectedItems() {
        Bundle bundle = getIntent().getExtras();
        ArrayList<TelName> selectedItems = (ArrayList<TelName>) bundle.getSerializable("telName");
        return selectedItems;
    }

    @Override
    public List<LocationInfo> getLocationInfos() {
        return null;
    }

}
