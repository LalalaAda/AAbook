package com.atally.thisgood;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.atally.models.P_consume;
import com.atally.models.P_income;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Date;

/**
 * Created by Paul-Sartre on 2015/12/16.
 */
public class ChartActivity extends AppCompatActivity {
    private PieChart mChart;
    private Spinner tu_sp;
    private int selected;
    String[] types=new String[]{"本月支出比重","本月收入比重","本年支出比重","本年收入比重"};
    String[] params=new String[]{"一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "约会", "网购", "日用品", "香烟",
            "工资", "外快兼职", "奖金", "借入", "零花钱", "投资收入", "礼物红包"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.mychart);

        mChart=(PieChart)findViewById(R.id.spread_pie_chart);
        tu_sp=(Spinner)findViewById(R.id.tu_sp);
        tu_sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types));
        // 完成饼图 数据的采集
        tu_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                PieData mPieData = getPieData(12, 100);
                showChart(mChart, mPieData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected=0;
                PieData mPieData=getPieData(12,100);
                showChart(mChart, mPieData);
            }
        });

    }
    private void showChart(PieChart pieChart,PieData pieData){
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60f);//半径
        pieChart.setTransparentCircleRadius(64f);//半透明圈
        pieChart.setDescription("账目饼状图");
        pieChart.setDrawCenterText(true);//饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90);//初始旋转角度
        pieChart.setRotationEnabled(true);//可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比
        pieChart.setCenterText("各项类型占比重");//饼状图中间的文字
        //设置数据
        pieChart.setData(pieData);
        Legend mLegend=pieChart.getLegend();//设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);//最右边显示
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        pieChart.animateXY(1000, 1000);
    }
    private PieData getPieData(int count,float range){

        //饼图数据
        float q1=0;
        float q2=0;
        float q3=0;
        float q4=0;
        float q5=0;
        float q6=0;
        float q7=0;
        float q8=0;
        float q9=0;
        float q10=0;
        float q11=0;
        float q12=0;
        switch (selected){
            case 0:
                Map<String,Double> ka=getConsumes(1);
                double total=ka.get("总计");
                q1=(float)((ka.get("一般")/total)*100);
                q2=(float)((ka.get("用餐")/total)*100);
                q3=(float)((ka.get("零食")/total)*100);
                q4=(float)((ka.get("交通")/total)*100);
                q5=(float)((ka.get("充值")/total)*100);
                q6=(float)((ka.get("购物")/total)*100);
                q7=(float)((ka.get("娱乐")/total)*100);
                q8=(float)((ka.get("住房")/total)*100);
                q9=(float)((ka.get("约会")/total)*100);
                q10=(float)((ka.get("网购")/total)*100);
                q11=(float)((ka.get("日用品")/total)*100);
                q12=(float)((ka.get("香烟")/total)*100);
                break;
            case 1:
                Map<String,Double> ka1=getIncomes(1);
                double total1=ka1.get("总计");
                q1=(float)((ka1.get("工资")/total1)*100);
                q2=(float)((ka1.get("外快兼职")/total1)*100);
                q3=(float)((ka1.get("奖金")/total1)*100);
                q4=(float)((ka1.get("借入")/total1)*100);
                q5=(float)((ka1.get("零花钱")/total1)*100);
                q6=(float)((ka1.get("投资收入")/total1)*100);
                q7=(float)((ka1.get("礼物红包")/total1)*100);
                q8=0;
                q9=0;
                q10=0;
                q11=0;
                q12=0;
                break;
            case 2:
                Map<String,Double> ka2=getConsumes(2);
                double total2=ka2.get("总计");
                q1=(float)((ka2.get("一般")/total2)*100);
                q2=(float)((ka2.get("用餐")/total2)*100);
                q3=(float)((ka2.get("零食")/total2)*100);
                q4=(float)((ka2.get("交通")/total2)*100);
                q5=(float)((ka2.get("充值")/total2)*100);
                q6=(float)((ka2.get("购物")/total2)*100);
                q7=(float)((ka2.get("娱乐")/total2)*100);
                q8=(float)((ka2.get("住房")/total2)*100);
                q9=(float)((ka2.get("约会")/total2)*100);
                q10=(float)((ka2.get("网购")/total2)*100);
                q11=(float)((ka2.get("日用品")/total2)*100);
                q12=(float)((ka2.get("香烟")/total2)*100);
                break;
            case 3:
                Map<String,Double> ka3=getIncomes(2);
                double total3=ka3.get("总计");
                q1=(float)((ka3.get("工资")/total3)*100);
                q2=(float)((ka3.get("外快兼职")/total3)*100);
                q3=(float)((ka3.get("奖金")/total3)*100);
                q4=(float)((ka3.get("借入")/total3)*100);
                q5=(float)((ka3.get("零花钱")/total3)*100);
                q6=(float)((ka3.get("投资收入")/total3)*100);
                q7=(float)((ka3.get("礼物红包")/total3)*100);
                q8=0;
                q9=0;
                q10=0;
                q11=0;
                q12=0;
                break;
        }

        ArrayList<String> xValues=new ArrayList<String>();//xValues用来表示每个饼块上的内容
//        for (int i=0;i<count;i++){
//            xValues.add(params[i]);//饼块上显示成项目1,2,3,4
//        }
        ArrayList<Entry> yValues=new ArrayList<Entry>();//yValues用来表示封装每个饼块的实际数据
        ArrayList<Integer> colors=new ArrayList<Integer>();
        //获取值大于0的数据
        if (q1>0){xValues.add(params[0]);yValues.add(new Entry(q1,0));}
        if (q2>0){xValues.add(params[1]);yValues.add(new Entry(q2,1));}
        if (q3>0){xValues.add(params[2]);yValues.add(new Entry(q3,2));}
        if (q4>0){xValues.add(params[3]);yValues.add(new Entry(q4,3));}
        if (q5>0){xValues.add(params[4]);yValues.add(new Entry(q5,4));}
        if (q6>0){xValues.add(params[5]);yValues.add(new Entry(q6,5));}
        if (q7>0){xValues.add(params[6]);yValues.add(new Entry(q7,6));}
        if (q8>0){xValues.add(params[7]);yValues.add(new Entry(q8,7));}
        if (q9>0){xValues.add(params[8]);yValues.add(new Entry(q9,8));}
        if (q10>0){xValues.add(params[9]);yValues.add(new Entry(q10,9));}
        if (q11>0){xValues.add(params[10]);yValues.add(new Entry(q11,10));}
        if (q12>0){xValues.add(params[11]);yValues.add(new Entry(q12,11));}

//        yValues.add(new Entry(q1,0));
//        yValues.add(new Entry(q2,1));
//        yValues.add(new Entry(q3,2));
//        yValues.add(new Entry(q4,3));
//        yValues.add(new Entry(q5,4));
//        yValues.add(new Entry(q6,5));
//        yValues.add(new Entry(q7,6));
//        yValues.add(new Entry(q8,7));
//        yValues.add(new Entry(q9,8));
//        yValues.add(new Entry(q10,9));
//        yValues.add(new Entry(q11, 10));
        //y轴的集合
        PieDataSet pieDataSet=new PieDataSet(yValues,types[selected]);//显示在比例图上
        pieDataSet.setSliceSpace(0f);//设置每个饼状图之间的距离


        //饼图颜色
        colors.add(Color.rgb(205,205,205));
        colors.add(Color.rgb(114,188,223));
        colors.add(Color.rgb(255,123,124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(144,72,64));
        colors.add(Color.rgb(109,109,109));
        colors.add(Color.rgb(255,255,0));
        colors.add(Color.rgb(215,0,255));
        colors.add(Color.rgb(68,129,196));
        colors.add(Color.rgb(0,153,153));
        colors.add(Color.rgb(25,53,99));
        pieDataSet.setColors(colors);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float px=5*(metrics.densityDpi/160f);
        pieDataSet.setSelectionShift(px);//选中态多出的长度
        PieData pieData=new PieData(xValues,pieDataSet);
        pieData.setValueTextSize(12f);//设置字体大小
        pieData.setValueFormatter(new PercentFormatter());//设置百分比显示
        return pieData;
    }
    TimeZone tz=TimeZone.getDefault();
    Calendar c=Calendar.getInstance(tz);//获取utc时间
    final int tyear=c.get(Calendar.YEAR);//获取本年
    final int tmonth=c.get(Calendar.MONTH)+1;//获取月份，0表示1月
    final int fyear=tyear+1;
    final int fmonth=tmonth+1;
    private long timeStemp1;
    private long timeStemp2;
    SQLiteDatabase db= Connector.getDatabase();
    //获取支出或者收入  1表示月份
    private Map<String,Double> getConsumes(int a){
        Map<String,Double> dmap= new HashMap<>();
        double total=0.0;
        double aa[]=new double[12];
        for (int j=0;j<12;j++){
            aa[j]=0.0;
        }
        if (a==1){
            //a=1是月份查询
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            try{
                Date hh1=sdf.parse(tyear+"-"+tmonth);
                Date hh2=sdf.parse(tyear+"-"+fmonth);
                timeStemp1=hh1.getTime()/1000;
                timeStemp2=hh2.getTime()/1000;
            }catch (Exception e){
                Log.i("ChartActivity","时间转化失败");
            }
            List<P_consume> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_consume.class);
            for (int i=0;i<incomes.size();i++){
                if (incomes.get(i).getType().equals("一般")){aa[0]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("用餐")){aa[1]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("零食")){aa[2]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("交通")){aa[3]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("充值")){aa[4]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("购物")){aa[5]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("娱乐")){aa[6]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("住房")){aa[7]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("约会")){aa[8]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("网购")){aa[9]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("日用品")){aa[10]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("香烟")){aa[11]+=(incomes.get(i).getCoin())*-1;}
                total+=(incomes.get(i).getCoin())*-1;
            }
        }else {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            try{
                Date hh1=sdf.parse(tyear+"");
                Date hh2=sdf.parse(fyear+"");
                timeStemp1=hh1.getTime()/1000;
                timeStemp2=hh2.getTime()/1000;
            }catch (Exception e){
                Log.i("ChartActivity","时间转化失败");
            }
            List<P_consume> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_consume.class);
            for (int i=0;i<incomes.size();i++){
                if (incomes.get(i).getType().equals("一般")){aa[0]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("用餐")){aa[1]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("零食")){aa[2]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("交通")){aa[3]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("充值")){aa[4]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("购物")){aa[5]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("娱乐")){aa[6]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("住房")){aa[7]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("约会")){aa[8]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("网购")){aa[9]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("日用品")){aa[10]+=(incomes.get(i).getCoin())*-1;}
                if (incomes.get(i).getType().equals("香烟")){aa[11]+=(incomes.get(i).getCoin())*-1;}
                total+=(incomes.get(i).getCoin())*-1;
            }
        }
        db.close();
        dmap.put("总计", total);
        dmap.put("一般",aa[0]>0?aa[0]:0.0);
        dmap.put("用餐",aa[1]>0?aa[1]:0.0);
        dmap.put("零食",aa[2]>0?aa[2]:0.0);
        dmap.put("交通",aa[3]>0?aa[3]:0.0);
        dmap.put("充值",aa[4]>0?aa[4]:0.0);
        dmap.put("购物",aa[5]>0?aa[5]:0.0);
        dmap.put("娱乐",aa[6]>0?aa[6]:0.0);
        dmap.put("住房",aa[7]>0?aa[7]:0.0);
        dmap.put("约会",aa[8]>0?aa[8]:0.0);
        dmap.put("网购",aa[9]>0?aa[9]:0.0);
        dmap.put("日用品",aa[10]>0?aa[10]:0.0);
        dmap.put("香烟",aa[11]>0?aa[11]:0.0);
        return dmap;

    }
    private Map<String,Double> getIncomes(int b){
        Map<String,Double> dmap= new HashMap<>();
        double total=0.0;
        double aa[]=new double[12];
        for (int j=0;j<12;j++){
            aa[j]=0.0;
        }
        if (b==1){
            //a=1是月份查询
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            try{
                Date hh1=sdf.parse(tyear+"-"+tmonth);
                Date hh2=sdf.parse(tyear+"-"+fmonth);
                timeStemp1=hh1.getTime()/1000;
                timeStemp2=hh2.getTime()/1000;
            }catch (Exception e){
                Log.i("ChartActivity","时间转化失败");
            }
            List<P_income> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
            for (int i=0;i<incomes.size();i++){
                if (incomes.get(i).getType().equals("工资")){aa[0]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("外快兼职")){aa[1]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("奖金")){aa[2]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("借入")){aa[3]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("零花钱")){aa[4]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("投资收入")){aa[5]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("礼物红包")){aa[6]+=(incomes.get(i).getCoin());}

                total+=(incomes.get(i).getCoin());
            }
        }else {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            try{
                Date hh1=sdf.parse(tyear+"");
                Date hh2=sdf.parse(fyear+"");
                timeStemp1=hh1.getTime()/1000;
                timeStemp2=hh2.getTime()/1000;
            }catch (Exception e){
                Log.i("ChartActivity","时间转化失败");
            }
            List<P_income> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
            for (int i=0;i<incomes.size();i++){
                if (incomes.get(i).getType().equals("工资")){aa[0]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("外快兼职")){aa[1]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("奖金")){aa[2]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("借入")){aa[3]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("零花钱")){aa[4]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("投资收入")){aa[5]+=(incomes.get(i).getCoin());}
                if (incomes.get(i).getType().equals("礼物红包")){aa[6]+=(incomes.get(i).getCoin());}
                total+=(incomes.get(i).getCoin());
            }
        }
        db.close();
        dmap.put("总计", total);
        dmap.put("工资",aa[0]>0?aa[0]:0.0);
        dmap.put("外快兼职",aa[1]>0?aa[1]:0.0);
        dmap.put("奖金",aa[2]>0?aa[2]:0.0);
        dmap.put("借入",aa[3]>0?aa[3]:0.0);
        dmap.put("零花钱",aa[4]>0?aa[4]:0.0);
        dmap.put("投资收入",aa[5]>0?aa[5]:0.0);
        dmap.put("礼物红包",aa[6]>0?aa[6]:0.0);
        dmap.put("",aa[7]>0?aa[7]:0.0);
        dmap.put("",aa[8]>0?aa[8]:0.0);
        dmap.put("",aa[9]>0?aa[9]:0.0);
        dmap.put("",aa[10]>0?aa[10]:0.0);
        dmap.put("",aa[11]>0?aa[11]:0.0);
        return dmap;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(ChartActivity.this,MainActivity.class);
            finish();
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }
}
