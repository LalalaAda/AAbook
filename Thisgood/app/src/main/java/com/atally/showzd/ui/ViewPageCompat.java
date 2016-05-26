package com.atally.showzd.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tuzhi on 4/7/2016.
 */
public class ViewPageCompat extends ViewPager{

    //mViewTouchMode表示ViewPager是否全权控制滑动事件，默认为false，即不控制
    private boolean mViewTouchMode=false;
    public ViewPageCompat(Context context) {
        super(context);
    }
    public ViewPageCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setViewTouchMode(boolean b){
        if (b&&!isFakeDragging()){
            //全权控制滑动事件
            beginFakeDrag();
        }else if (!b&&isFakeDragging()){
            //终止控制滑动事件
            endFakeDrag();
        }
        mViewTouchMode=b;
    }
    //在mViewTouchMode为true时，ViewPager不拦截点击事件，点击事件将由子view处理

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewTouchMode){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        }catch (Exception e){
            return false;
        }
    }
    //在mViewTouchMode为true时或者滑动方向不是左右时,ViewPager将放弃控制点击事件
    //这样有利于在ViewPager中加入listView等可以滑动的控件，否则两者之间的滑动会冲突

    @Override
    public boolean arrowScroll(int direction) {
        if (mViewTouchMode) return false;
        if (direction!=FOCUS_LEFT&&direction!=FOCUS_RIGHT)
            return false;
        return super.arrowScroll(direction);
    }
}
