package com.ytj.project_login;

import android.os.Bundle;
import android.util.Log;

import com.ytj.project_login.adapter.WithCheckBoxExpandableListAdapter;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2016/10/6.
 */
public class TeamBDMapActivity extends BaseBDMapActivity {
    private ArrayList<HashMap<Integer, Boolean>> isSelected;
    private ArrayList<ArrayList<TelName>> items;
    private Vector<TelName> selectedItems;

    @Override
    public Vector<TelName> getSelectedItems() {
        selectedItems = new Vector<TelName>();

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

        return selectedItems;
    }

    @Override
    public List<LocationInfo> getLocationInfos() {
        return null;
    }
}
