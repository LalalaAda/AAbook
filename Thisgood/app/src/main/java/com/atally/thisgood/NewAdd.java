package com.atally.thisgood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.atally.models.GridViewEntity;
import com.atally.models.P_consume;
import com.atally.models.P_income;
import com.atally.myview.GridViewAdapter;

import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewAdd extends AppCompatActivity implements View.OnClickListener {
    private GridViewEntity entity;
    private GridViewAdapter adapter;
    private GridView gridView;
    private Integer[] img;
    private String[] st;
    private List<GridViewEntity> list;

    private Button pay_btn, income_btn;
    private TextView changedshow_text;
    private ImageView changedshow_jpg;
    private MyGridLister grlister;

    private EditText coin;
    private Button add_date;
    private Button add_time;
    private Button ok;
    private Button cancel;
    private EditText add_et;
    private String user = "";
    private int tableName = 0;
    private Date atime;
    private long timeStemp;
    private String amark = "";
    private String atype = "一般";
//    String[] types = new String[]{"日常消费","衣服装饰", "工资奖金", "投资盈利", "出行交通",
//            "娱乐聚会", "生活用品", "水电房租", "缴费清单", "股票收益", "其他"};
    Context context;
    SQLiteDatabase db = Connector.getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.newadd);
        context = NewAdd.this;
        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
        //name
        user = sp.getString("user", "user");
        coin = (EditText) findViewById(R.id.add_tx1);
        add_date = (Button) findViewById(R.id.add_date);
        add_time = (Button) findViewById(R.id.add_time);
        ok = (Button) findViewById(R.id.add_ok);
        cancel = (Button) findViewById(R.id.add_cancel);
        add_et = (EditText) findViewById(R.id.add_et);
        Intent a = (Intent) getIntent();
        String aa = a.getStringExtra("Result");//接收来自Myc传来的值
        if(Double.valueOf(aa)<0){
            //假如穿过来的是负数 转为正数
            aa=String.valueOf((Double.valueOf(aa)) * -1);
        }
        //金额coin
        coin.setText(aa);

        //时间atime="";
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取东八区时间
        final int tyear = c.get(Calendar.YEAR); // 获取年
        final int tmonth = c.get(Calendar.MONTH); // 获取月份，0表示1月份
        final int tday = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
        final int thour=c.get(Calendar.HOUR_OF_DAY);
        final int tminute=c.get(Calendar.MINUTE);
        final int ss=c.get(Calendar.SECOND);//获取秒
        int tmonth2=tmonth+1;
        add_date.setText(tyear+"-"+tmonth2+"-"+tday);
        add_time.setText(thour+":"+tminute);
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_date.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
//                        Log.i("MainActivity","成功添加");
                    }
                }, tyear, tmonth, tday).show();
            }
        });
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewAdd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_time.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, thour, tminute, true).show();
            }
        });

        initView();
        initSelectColor();
        //确定添加账目  时间戳是根据gmt+8:00表示的。
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //备注
                if (add_et.getText() == null) {
                    amark = "";
                } else {
                    amark = add_et.getText().toString();
                }

                String pmTime = add_date.getText().toString() + " " + add_time.getText().toString() + ":00";
//                Log.i("MainActivity",pmTime);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    atime = df.parse(pmTime);//转成utc之后时间为+8小时
//                    timeStemp=(atime.getTime()-28800000)/1000;//将时间转换成utc时间戳并去毫秒数
                    timeStemp = (atime.getTime() / 1000);
//                    Log.i("MainActivity",String.valueOf(timeStemp));
                } catch (Exception e) {
                    Log.i("MainActivity", "时间转换失败");
                }
                if (tableName == 1) {
                    P_income aincome = new P_income();
                    aincome.setName(user);
                    aincome.setCoin(Double.valueOf(coin.getText().toString()));
                    aincome.setMark(amark);
                    aincome.setType(atype);
                    aincome.setTime(timeStemp);
                    aincome.setCreates(pmTime + ":" + ss);//添加创建时间字符串 精确到秒
                    aincome.save();
                    if (aincome.save()) {
                        Toast.makeText(context, "添加一笔收入", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("last", 1);
                        editor.commit();
                    } else {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    P_consume aconsume = new P_consume();
                    aconsume.setName(user);
                    aconsume.setCoin(Double.valueOf(coin.getText().toString()) * -1);
                    aconsume.setMark(amark);
                    aconsume.setType(atype);
                    aconsume.setTime(timeStemp);
                    aconsume.setCreates(pmTime + ":" + ss);
                    aconsume.save();
                    if (aconsume.save()) {
                        Toast.makeText(context, "添加一笔支出", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("last", 2);
                        editor.commit();
                    } else {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
                Intent ina = new Intent(NewAdd.this, MainActivity.class);
                startActivity(ina);
                NewAdd.this.finish();
            }
        });
        //取消添加账目
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent ina = new Intent(NewAdd.this, MainActivity.class);
                startActivity(ina);
                NewAdd.this.finish();
            }
        });
    }

    private void initView(){
        gridView = (GridView) findViewById(R.id.gridview);
        income_btn = (Button) findViewById(R.id.income_btn);
        pay_btn = (Button) findViewById(R.id.pay_btn);
        income_btn.setOnClickListener(this);
        pay_btn.setOnClickListener(this);
        changedshow_text = (TextView) findViewById(R.id.changedshow_text);
        changedshow_jpg = (ImageView) findViewById(R.id.changedshow_jpg);
        grlister = new MyGridLister();
        gridView.setOnItemClickListener(grlister);

        img = new Integer[] { R.drawable.type_big_1, R.drawable.type_big_2, R.drawable.type_big_3,
                R.drawable.type_big_4, R.drawable.type_big_5, R.drawable.type_big_6, R.drawable.type_big_7,
                R.drawable.type_big_8, R.drawable.type_big_9, R.drawable.type_big_10, R.drawable.type_big_11,
                R.drawable.type_big_12 };
        st = new String[] { "一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "约会", "网购", "日用品", "香烟" };
        entity = new GridViewEntity(img, st);
        list = new ArrayList<GridViewEntity>();
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        adapter = new GridViewAdapter(list, this);
        gridView.setAdapter(adapter);
    }

    private void incomeIsClick() {
        img = new Integer[] { R.drawable.type_big_13, R.drawable.type_big_14, R.drawable.type_big_15,
                R.drawable.type_big_16, R.drawable.type_big_17, R.drawable.type_big_18, R.drawable.type_big_19 };
        st = new String[] { "工资", "外快兼职", "奖金", "借入", "零花钱", "投资收入", "礼物红包" };
        entity = new GridViewEntity(img, st);
        list = new ArrayList<GridViewEntity>();
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);
        list.add(entity);

        adapter = new GridViewAdapter(list, this);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }
    private void initSelectColor() {
        pay_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_blue));
        income_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_white));
        pay_btn.setTextColor(pay_btn.getResources().getColor(R.color.common_white));
        income_btn.setTextColor(income_btn.getResources().getColor(R.color.common_dark));

    }
    private class MyGridLister implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            entity = list.get(position);
            changedShow(entity, position);
            atype=st[position];
        }

    }
    /**
     * 此方法可展示的gridview单击结果
     */
    private void changedShow(GridViewEntity ent, int position) {

        Integer[] img = ent.getImgs();
        int path = img[position];
        changedshow_jpg.setImageResource(path);
        String[] st = ent.getTypes();
        String text = st[position];
        changedshow_text.setText(text);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_btn:
                initView();
                pay_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_blue));
                income_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_white));
                pay_btn.setTextColor(pay_btn.getResources().getColor(R.color.common_white));
                income_btn.setTextColor(income_btn.getResources().getColor(R.color.common_dark));
                tableName=0;
                break;
            case R.id.income_btn:
                incomeIsClick();
                income_btn.setBackgroundColor(income_btn.getResources().getColor(R.color.common_blue));
                pay_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_white));
                income_btn.setTextColor(income_btn.getResources().getColor(R.color.common_white));
                pay_btn.setTextColor(pay_btn.getResources().getColor(R.color.common_dark));
                tableName=1;
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent in1 = new Intent(NewAdd.this, MainActivity.class);
            NewAdd.this.finish();
            startActivity(in1);

        }
        return super.onKeyDown(keyCode, event);
    }
}
