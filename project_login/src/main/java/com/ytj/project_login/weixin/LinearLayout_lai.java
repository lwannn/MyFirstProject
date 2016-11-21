package com.ytj.project_login.weixin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ytj.project_login.R;

import java.util.Map;

/**
 * Created by ddanyang on 2016/11/7.
 */

public class LinearLayout_lai extends LinearLayout {

    //添加的View
    ImageView imageView;
    TextView textView;
    //接口定义
    ControlRelevant controlRelevant;


    //自定义一个构造函数，用于Java代码动态生成（可以在外面代码生成xml然后转成attributeset后面尝试）
    public LinearLayout_lai(Context context, Map<String, String> attrs_map, ControlRelevant control) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);

        controlRelevant = control;
        Information info = new Information(attrs_map);
        initImageView(context, info.imageSrcYes, info.imageSrcNo, info.imageSize);
        initTextView(context, info.textSrc, info.textColorYes, info.textColorNo, info.textSize);
    }

    public LinearLayout_lai(Context context) {
        this(context, null);
    }

    public LinearLayout_lai(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout_lai(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);

        Information info = new Information(context, attrs);

        //本来准备采用对sttrs进行动态生成的方法来new一个View的（因为觉得这样应该会减少一次View的绘制快一些），但是为了加快进度先放弃
        //对ImageView和TextView进行初始化,并加入到本布局
        initImageView(context, info.imageSrcYes, info.imageSrcNo, info.imageSize);
        initTextView(context, info.textSrc, info.textColorYes, info.textColorNo, info.textSize);
    }

    //传递px值过来
    private void initImageView(Context context, int src_yes, int src_no, int imageSize) {
        imageView = new ImageView(context);
        LayoutParams params_image = new LayoutParams(imageSize, imageSize);
        params_image.bottomMargin = 4;
        params_image.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(params_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(getStateListDrawable(0, src_yes, src_no));
        }else {
            imageView.setBackgroundDrawable(getStateListDrawable(0, src_yes, src_no));
        }
        addView(imageView);
    }

    private void initTextView(Context context, String textSrc, int src_yes, int src_no, int textSize) {
        textView = new TextView(context);
        LayoutParams params_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_text.gravity = Gravity.CENTER_HORIZONTAL;
        params_text.bottomMargin = 4;
        textView.setLayoutParams(params_text);
        textView.setText(textSrc);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(getColorStateList(src_yes, src_no));

        addView(textView);
    }

    //生成SelectDrawable
    private StateListDrawable getStateListDrawable(int type, int yes, int no) {
        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_selected}, getResources().getDrawable(yes));//mResources在49行处被执行（super(context, attrs, defStyleAttr)）
        listDrawable.addState(new int[]{-android.R.attr.state_selected}, getResources().getDrawable(no));
        return listDrawable;
    }

    //
    private ColorStateList getColorStateList(int yes, int no) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}}
                , new int[]{yes, no});
        return colorStateList;
    }

    @Override
    public boolean performClick() {
        //对布局进行样式进行变换
        if (!imageView.isSelected()) {
            imageView.setSelected(true);
            textView.setSelected(true);
        }
        //是否执行回调父控件方法
        boolean isConsumeEvent = false;
        if (controlRelevant != null) {
            isConsumeEvent = controlRelevant.controlRelevant(this);
        }
        //点击事件是否已在父控件消费
        if (!isConsumeEvent) {
            return super.performClick();
        }else {
            return isConsumeEvent;
        }
    }

    public void restoreUI() {
        imageView.setSelected(false);
        textView.setSelected(false);
    }

    interface ControlRelevant {
        //返回true说明已经在外部执行了触摸事件，将不再执行后面代码
        boolean controlRelevant(View v);
    }

    //自定义属性封装类
    private class Information {
        int imageSize;
        int imageSrcYes;
        int imageSrcNo;
        String textSrc;
        int textColorYes;
        int textColorNo;
        int textSize;

        public Information(Map<String, String> attrs) {
            imageSize = Integer.valueOf(attrs.get("imageSize"));
            imageSrcYes = Integer.valueOf(attrs.get("imageSrcYes"));
            imageSrcNo = Integer.valueOf(attrs.get("imageSrcNo"));
            textSrc = attrs.get("textSrc");
            textColorYes = Integer.valueOf(attrs.get("textColorYes"));
            textColorNo = Integer.valueOf(attrs.get("textColorNo"));
            textSize = Integer.valueOf(attrs.get("textSize"));
        }

        public Information(Context context, AttributeSet attrs) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearLayout_lai);
            imageSrcYes = typedArray.getResourceId(R.styleable.LinearLayout_lai_image_src_yes, R.mipmap.ic_launcher);
            imageSrcNo = typedArray.getResourceId(R.styleable.LinearLayout_lai_image_src_no, R.mipmap.ic_launcher);
            imageSize = typedArray.getDimensionPixelSize(R.styleable.LinearLayout_lai_image_size, 48);
            textSrc = typedArray.getString(R.styleable.LinearLayout_lai_text_src);
            textColorYes = typedArray.getColor(R.styleable.LinearLayout_lai_text_color_yes, 0x000000);
            textColorNo = typedArray.getColor(R.styleable.LinearLayout_lai_text_color_yes, 0x888888);
            textSize = typedArray.getDimensionPixelSize(R.styleable.LinearLayout_lai_text_size, 18);
        }
    }
}
