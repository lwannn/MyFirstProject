package com.ytj.project_login;

import android.os.Bundle;

import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2016/10/6.
 */
public class PersonalBDMapActivity extends BaseBDMapActivity {

    static {
        IS_PERSONAL = 1;
    }

    @Override
    public Vector<TelName> getSelectedItems() {
        Vector<TelName> selectedItems = new Vector<TelName>();
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
