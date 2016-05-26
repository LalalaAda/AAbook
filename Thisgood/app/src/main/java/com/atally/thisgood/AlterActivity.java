package com.atally.thisgood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AlterActivity extends AppCompatActivity implements View.OnClickListener {
    private GridViewEntity entity;
    private GridViewAdapter adapter;
    private GridView gridView;
    private Integer[] img;
    private String[] st;
    private List<GridViewEntity> list;

    private Button pay_btn,income_btn;
    private TextView changedshow_text;
    private ImageView changedshow_jpg;
    private MyGridLister grlister;

    private EditText coin;
    private Button add_date;
    private Button add_time;
    private Button ok;
    private Button cancel;
    private EditText add_et;
    private String type;
    private int id;
    private String user = "";
    private int tableName = 0;
    private Date atime;
    private long timeStemp;
    private String amark = "一般";
//    String[] types = new String[]{"日常消费","衣服装饰", "工资奖金", "投资盈利", "出行交通",
//            "娱乐聚会", "生活用品", "水电房租", "缴费清单", "股票收益", "其他"};
    Context context;
    SQLiteDatabase db = Connector.getDatabase();
    private P_income income;//收入表
    private P_consume consume;//支出表
    int year;
    int month;
    int day;
    int hour;
    int minute;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.newadd);
        context = AlterActivity.this;
        coin = (EditText) findViewById(R.id.add_tx1);//金额
        add_date = (Button) findViewById(R.id.add_date);//日期
        add_time = (Button) findViewById(R.id.add_time);//时间
        ok = (Button) findViewById(R.id.add_ok);
        cancel = (Button) findViewById(R.id.add_cancel);
        add_et = (EditText) findViewById(R.id.add_et);//备注
        Intent x = (Intent) getIntent();
        tableName = x.getIntExtra("table", 0);//0是支出1是收入
        id = x.getIntExtra("id", 0);
        TimeZone tz=TimeZone.getDefault();
        Calendar c = Calendar.getInstance(tz);
        initView();
        initSelectColor();
        if (tableName == 0) {
            consume = DataSupport.find(P_consume.class, id);
            coin.setText(String.valueOf(consume.getCoin() * -1));//金额设置
            user = consume.getName();
            c.setTime(new java.util.Date((consume.getTime()) * 1000));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            type = consume.getType();
            //设置点击选中的类型
            Integer[] ccimg=new Integer[]{R.drawable.type_big_1, R.drawable.type_big_2, R.drawable.type_big_3,
                    R.drawable.type_big_4, R.drawable.type_big_5, R.drawable.type_big_6, R.drawable.type_big_7,
                    R.drawable.type_big_8, R.drawable.type_big_9, R.drawable.type_big_10, R.drawable.type_big_11,
                    R.drawable.type_big_12 };
            String[] ccst=new String[]{"一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "约会",
                    "网购", "日用品", "香烟"};
            for (int i=0;i<ccst.length;i++){
                if (type.equals(ccst[i])){
                    changedshow_jpg.setImageResource(ccimg[i]);
                    changedshow_text.setText(ccst[i]);
                }
            }
            add_et.setText(consume.getMark().toString());
            add_date.setText(year + "-" + month + "-" + day);
            add_time.setText(hour+":"+minute);

        } else {
            income = DataSupport.find(P_income.class, id);
            coin.setText(String.valueOf(income.getCoin()));//金额设置
            user = income.getName();
            c.setTime(new java.util.Date((income.getTime()) * 1000));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            type = income.getType();

            Integer[] cbimg=new Integer[]{ R.drawable.type_big_13, R.drawable.type_big_14, R.drawable.type_big_15,
                    R.drawable.type_big_16, R.drawable.type_big_17, R.drawable.type_big_18, R.drawable.type_big_19 };
            String[] cbst=new String[]{"工资", "外快兼职", "奖金", "借入", "零花钱", "投资收入", "礼物红包" };
            for (int i=0;i<cbst.length;i++){
                if (type.equals(cbst[i])){
                    changedshow_jpg.setImageResource(cbimg[i]);
                    changedshow_text.setText(cbst[i]);
                }
            }
            add_et.setText(income.getMark().toString());
            incomeIsClick();
            income_btn.setBackgroundColor(income_btn.getResources().getColor(R.color.common_blue));
            pay_btn.setBackgroundColor(pay_btn.getResources().getColor(R.color.common_white));
            income_btn.setTextColor(income_btn.getResources().getColor(R.color.common_white));
            pay_btn.setTextColor(pay_btn.getResources().getColor(R.color.common_dark));
            add_date.setText(year+"-"+month+"-"+day);
            add_time.setText(hour+":"+minute);
        }
        //日期时间
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_date.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
            }
        });
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AlterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_time.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, hour, minute, true).show();
            }
        });
        //确定修改账目
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    atime = df.parse(pmTime);
                    timeStemp = (atime.getTime() / 1000);
                } catch (Exception e) {
                    Log.i("MainActivity", "时间转换失败");
                }
                if (tableName == 1) {

                    ContentValues values=new ContentValues();
                    values.put("coin",Double.valueOf(coin.getText().toString()));
                    values.put("name",user);
                    values.put("time",timeStemp);
                    values.put("type",type);
                    values.put("mark", amark);
                    int a=DataSupport.update(P_income.class,values,id);
                    if (a>0) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    ContentValues values=new ContentValues();
                    values.put("coin", Double.valueOf(coin.getText().toString())*-1);
                    values.put("name", user);
                    values.put("time", timeStemp);
                    values.put("type", type);
                    values.put("mark", amark);
                    int b=DataSupport.update(P_consume.class,values,id);
                    if (b>0) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
                Intent ina = new Intent(AlterActivity.this, MainActivity.class);
                AlterActivity.this.finish();
                startActivity(ina);

            }
        });
        //取消添加账目
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent ina = new Intent(AlterActivity.this, MainActivity.class);
                AlterActivity.this.finish();
                startActivity(ina);

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

    private class MyGridLister implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            entity = list.get(position);
            changedShow(entity, position);
            type=st[position];
        }

    }
    private void changedShow(GridViewEntity ent, int position) {

        Integer[] img = ent.getImgs();
        int path = img[position];
        changedshow_jpg.setImageResource(path);
        String[] st = ent.getTypes();
        String text = st[position];
        changedshow_text.setText(text);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent in1 = new Intent(AlterActivity.this, MainActivity.class);
            AlterActivity.this.finish();
            startActivity(in1);

        }
        return super.onKeyDown(keyCode, event);
    }
}
