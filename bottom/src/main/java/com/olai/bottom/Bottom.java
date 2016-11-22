package com.olai.bottom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.a0lai.wecourse.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddanyang on 2016/11/7.
 */

public class Bottom extends LinearLayout implements LinearLayout_lai.ControlRelevant {

    /**
     * 定义变量，自定义的attr属性
     */
    private LinearLayout_lai[] bottomItems;//底部button的引用
    private int itemNumber;

    public Bottom(Context context) {
        this(context, null);
    }

    public Bottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Bottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Information info = new Information(context, attrs);
        itemNumber = info.itemNumber;
        bottomItems = getBottomItems(context, info, itemNumber);//数据已经加载完成
    }

    //生成布局
    private LinearLayout_lai[] getBottomItems(Context context, Information info, int itemNumber) {
        if (bottomItems != null) {
            if (bottomItems.length == itemNumber) {
                return bottomItems;
            }
        }
        Map<String, String> attrs = new HashMap<>();
        attrs.put("imageSize", String.valueOf(info.itemImageSize));
        attrs.put("textColorYes", String.valueOf(info.itemTextColorYes));
        attrs.put("textColorNo", String.valueOf(info.itemTextColorNo));
        attrs.put("textSize", String.valueOf(info.itemTextSize));
        LinearLayout_lai[] bottomItems = new LinearLayout_lai[itemNumber];
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        for (int i = 0; i < itemNumber; i++) {
            Map<String, String> _attrs = new HashMap<>(attrs);
            _attrs.put("imageSrcYes", String.valueOf(info.itemImagesSrcYes[i]));
            _attrs.put("imageSrcNo", String.valueOf(info.itemImagesSrcNo[i]));
            _attrs.put("textSrc", String.valueOf(info.itemTextsSrc[i]));
            bottomItems[i] = new LinearLayout_lai(context, _attrs, this);
            bottomItems[i].setLayoutParams(params);
            bottomItems[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            addView(bottomItems[i]);
        }
        return bottomItems;
    }

    public Bottom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    //设置初始化界面时处于选择状态的按钮,从0开始
    public void setSelectedItem(int index) {
        controlRelevant(bottomItems[index]);
    }

    View oldSelected;
    @Override
    public boolean controlRelevant(View view) {
        boolean isConsumeEvent = false;
        for (LinearLayout_lai linear : bottomItems) {
            if (view == oldSelected) {
                isConsumeEvent = true;
            }else {
                if (linear == oldSelected) {
                    linear.restoreUI();
                }
            }
        }
        oldSelected = view;
        return isConsumeEvent;
    }

    //通过HashMap实现
//    int oldSelectedKey;
//    @Override
//    public boolean controlRelevant(View view) {
//        Map<Integer, LinearLayout_lai> map = new HashMap<>();
//        for (LinearLayout_lai l : bottomItems) {
//            map.put(l.hashCode(), l);
//        }
//        boolean isConsumeEvent;
//        if (oldSelectedKey == view.hashCode()) {
//            isConsumeEvent = true;
//        } else {
//            map.get(oldSelectedKey).restoreUI();
//            isConsumeEvent = false;
//        }
//        return isConsumeEvent;
//    }



    private class Information {
        int itemNumber;
        int[] itemImagesSrcYes;
        int[] itemImagesSrcNo;
        int itemImageSize;
        String[] itemTextsSrc;
        int itemTextColorYes;
        int itemTextColorNo;
        int itemTextSize;

        public Information(Context context, AttributeSet attrs) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bottom);
            itemNumber = typedArray.getInteger(R.styleable.Bottom_item_number, 1);

            String packageName = context.getPackageName();
            String[] _itemImagesSrcYes = typedArray.getString(R.styleable.Bottom_item_images_src_yes).split(";");
            String[] _itemImagesSrcNo = typedArray.getString(R.styleable.Bottom_item_images_src_no).split(";");
            itemImagesSrcYes = new int[_itemImagesSrcYes.length];
            itemImagesSrcNo = new int[_itemImagesSrcNo.length];

            for (int i = 0; i < _itemImagesSrcYes.length; i++) {
                itemImagesSrcYes[i] = context.getResources().getIdentifier(packageName + ":mipmap/" + _itemImagesSrcYes[i], null, null);
                itemImagesSrcNo[i] = context.getResources().getIdentifier(packageName + ":mipmap/" + _itemImagesSrcNo[i], null, null);
            }
            itemImageSize = (int) typedArray.getDimension(R.styleable.Bottom_item_images_size, 1);
            itemTextsSrc = typedArray.getString(R.styleable.Bottom_item_texts_src).split(";");
            int i = 0;
            try {
                i++;
                itemTextColorYes = typedArray.getColor(R.styleable.Bottom_item_text_color_yes, 0xffffff);
                i++;
                itemTextColorNo = typedArray.getColor(R.styleable.Bottom_item_text_color_no, 0x888888);
            } catch (Resources.NotFoundException e) {
                switch (i) {
                    case 1:
                        itemTextColorYes = typedArray.getInt(R.styleable.Bottom_item_text_color_yes, 0xffffffff)|0xff000000;
                    case 2:
                        itemTextColorNo = typedArray.getInt(R.styleable.Bottom_item_text_color_no, 0xffffffff)|0xff000000;
                        break;
                }
            }

            itemTextSize = typedArray.getDimensionPixelSize(R.styleable.Bottom_item_text_size, 18);
        }
    }

}
