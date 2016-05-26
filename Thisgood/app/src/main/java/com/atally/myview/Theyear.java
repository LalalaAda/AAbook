package com.atally.myview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atally.models.P_consume;
import com.atally.models.P_income;
import com.atally.models.P_the;
import com.atally.showzd.MyListViewAdapter;
import com.atally.thisgood.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Theyear extends Fragment {
    private ListView listView;
    private static Context acontext;
    private long timeStemp1;
    private long timeStemp2;
    SQLiteDatabase db = Connector.getDatabase();

    public Theyear(){super();}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        acontext=activity.getApplicationContext();
    }
    TimeZone tz=TimeZone.getDefault();
    Calendar c = Calendar.getInstance(tz);//获取utc时间
    final int tyear = c.get(Calendar.YEAR); // 获取年
    final int fyear=tyear+1;
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_one,container,false);
        listView= (ListView) rootView.findViewById(R.id.oklist);
        try {
            Date hh2 = simpleDateFormat.parse(fyear+"");
            Date hh1=simpleDateFormat.parse(tyear+"");
            timeStemp1=hh1.getTime()/1000;
            timeStemp2=hh2.getTime()/1000;
//            Log.i("MainActivity","最小年"+String.valueOf(timeStemp1));
//            Log.i("MainActivity", "最大年"+String.valueOf(timeStemp2));
        }catch(Exception e){
            Log.i("MainActivity","时间错误");
        }
        List<P_income> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
        List<P_consume> consumes= DataSupport.where("time between ? and ?",String.valueOf(timeStemp1),String.valueOf(timeStemp2)).find(P_consume.class);
        SharedPreferences sp = getActivity().getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        String zb=sp.getString("user", "默认账本");
        List<P_income> newIncomes=new ArrayList<>();
        List<P_consume> newConsume=new ArrayList<>();
        for(int i=0;i<incomes.size();i++){
            if (incomes.get(i).getName().equals(zb))
                newIncomes.add(incomes.get(i));
        }
        for (int j=0;j<consumes.size();j++){
            if (consumes.get(j).getName().equals(zb))
                newConsume.add(consumes.get(j));
        }
        db.close();

        final List<P_the> plist=new ArrayList<>();
        for (int i = newIncomes.size()-1; i >= 0; i--) {
            //Log.i("MainActivity","0000"+i);
            P_the pp = new P_the();
            pp.setBigtype(0);
            pp.setId(newIncomes.get(i).getId());
            pp.setType(newIncomes.get(i).getType());
            pp.setCoin(newIncomes.get(i).getCoin());
            pp.setTime(newIncomes.get(i).getTime());

            plist.add(pp);
        }
        for (int j = newConsume.size()-1; j >= 0; j--) {
            //Log.i("MainActivity","1111"+j);
            P_the pq = new P_the();
            pq.setBigtype(1);
            pq.setId(newConsume.get(j).getId());
            pq.setType(newConsume.get(j).getType());
            pq.setCoin(newConsume.get(j).getCoin());
            pq.setTime(newConsume.get(j).getTime());
            plist.add(pq);
        }
        Collections.sort(plist);


        MyListViewAdapter adapter=new MyListViewAdapter(acontext,plist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (plist.get(position).getBigtype() == 0) {
                    new Change_in().show(getFragmentManager(), String.valueOf(plist.get(position).getId()));
                } else {
                    new Change_out().show(getFragmentManager(), String.valueOf(plist.get(position).getId()));
                }
            }
        });
        return rootView;
    }




}