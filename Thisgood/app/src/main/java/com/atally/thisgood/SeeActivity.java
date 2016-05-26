package com.atally.thisgood;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.atally.myview.Change_in;
import com.atally.myview.Change_out;
import com.atally.myview.Theday;
import com.atally.myview.Themonth;
import com.atally.myview.Theweek;
import com.atally.myview.Theyear;
import com.atally.showzd.ui.IndicatorFragmentActivity;

import java.util.List;

public class SeeActivity extends IndicatorFragmentActivity implements Change_in.ChangeInListener,Change_out.ChangeOutListener{

    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FOUR=3;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int supplyTabs(List<TabInfo> tabs) {
        tabs.add(new TabInfo(FRAGMENT_ONE, getString(R.string.fragment_one),
                Theday.class));
        tabs.add(new TabInfo(FRAGMENT_TWO, getString(R.string.fragment_two),
                Theweek.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, getString(R.string.fragment_three),
                Themonth.class));
        tabs.add(new TabInfo(FRAGMENT_FOUR,getString(R.string.fragment_four),
                Theyear.class));

        return FRAGMENT_ONE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(SeeActivity.this,MainActivity.class);
            finish();
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onInChange(int i) {
        if (i>0){
            Toast.makeText(SeeActivity.this,"删除一条记录成功", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,SeeActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onOutChange(int i) {
        if (i>0){
            Toast.makeText(SeeActivity.this,"删除一条记录成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,SeeActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
