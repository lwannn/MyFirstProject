package com.ytj.project_login;

import android.os.Bundle;

import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
public class PersonalBDMapActivity extends BaseBDMapActivity {
    @Override
    public ArrayList<TelName> getSelectedItems() {
        ArrayList<TelName> selectedItems = new ArrayList<TelName>();
        Bundle bundle = getIntent().getExtras();
        TelName telName = (TelName) bundle.getSerializable("telName");
        selectedItems.add(telName);
        return selectedItems;
    }

    @Override
    public List<LocationInfo> getLocationInfos() {
        return null;
    }
}
