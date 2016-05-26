package com.atally.showzd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atally.models.P_the;
import com.atally.thisgood.R;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<P_the> pList;

    public MyListViewAdapter(Context context,List<P_the> list){
        this.context=context;
        pList=list;
    }

    @Override
    public int getCount() {
        if (pList!=null){
            return pList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HodView hod;
        if (convertView==null){
            hod=new HodView();
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_item,null);
            hod.rightBillshow_text= (TextView) convertView.findViewById(R.id.rightbillshow_text);
            hod.leftBillshow_text= (TextView) convertView.findViewById(R.id.leftbillshow_text);
            hod.imgbill= (ImageView) convertView.findViewById(R.id.imgbill);
            hod.xian=(View)convertView.findViewById(R.id.xian);
            convertView.setTag(hod);
        }else{
            hod= (HodView) convertView.getTag();
        }
        if (pList.get(position).getBigtype()==0){
            hod.imgbill.setImageResource(findYourimg(0, pList.get(position).getType()));
            hod.leftBillshow_text.setText(pList.get(position).getType()+" "+pList.get(position).getCoin());
            hod.leftBillshow_text.setVisibility(View.VISIBLE);
        }else {
            hod.imgbill.setImageResource(findYourimg(1,pList.get(position).getType()));
            hod.rightBillshow_text.setText(pList.get(position).getType()+" "+pList.get(position).getCoin());
            hod.rightBillshow_text.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    private int findYourimg(int num,String name) {
        if (num == 1) {
            Integer[] img1 = new Integer[]{R.drawable.type_big_1, R.drawable.type_big_2, R.drawable.type_big_3,
                    R.drawable.type_big_4, R.drawable.type_big_5, R.drawable.type_big_6, R.drawable.type_big_7,
                    R.drawable.type_big_8, R.drawable.type_big_9, R.drawable.type_big_10, R.drawable.type_big_11,
                    R.drawable.type_big_12};
            String[] st1 = new String[]{"一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "约会", "网购", "日用品", "香烟"};
            for (int i=0;i<st1.length;i++){
                if (name.equals(st1[i]))
                    return img1[i];
            }
        }else{
            Integer[] img2 = new Integer[]{R.drawable.type_big_13, R.drawable.type_big_14, R.drawable.type_big_15,
                    R.drawable.type_big_16, R.drawable.type_big_17, R.drawable.type_big_18, R.drawable.type_big_19};
            String[] st2 = new String[]{"工资", "外快兼职", "奖金", "借入", "零花钱", "投资收入", "礼物红包"};
            for (int j=0;j<img2.length;j++){
                if (name.equals(st2[j]))
                    return img2[j];
            }
        }
        return 0;
    }

    private class HodView{
        TextView rightBillshow_text,leftBillshow_text;
        ImageView imgbill;
        View xian;
    }
}
