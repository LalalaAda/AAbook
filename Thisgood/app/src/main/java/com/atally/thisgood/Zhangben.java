package com.atally.thisgood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.atally.models.P_zhangben;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class Zhangben extends AppCompatActivity implements View.OnClickListener{
    private Button ok;
    private Button cancel;
    private TextView zb_add;
    private Spinner zb_sp;
    private String zbname;
    private Button zb_btn1;//选择
    private Button zb_btn2;//添加
    private View zb_view1;//选择
    private View zb_view2;//添加
    private int status=0;//默认0为选择1为添加
    private String[] zbnames;
    Context context;
    SQLiteDatabase db= Connector.getDatabase();
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhangben);
        initview();
        initSelectColor();
        sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
        //name
        zbname = sp.getString("user", "默认账本");

    }

    private void initview(){
        ok= (Button) findViewById(R.id.zb_ok);
        cancel= (Button) findViewById(R.id.zb_cancel);
        zb_add= (TextView) findViewById(R.id.zb_add);
        zb_sp= (Spinner) findViewById(R.id.zb_sp);
        zb_btn1= (Button) findViewById(R.id.zb_btn1);
        zb_btn2= (Button) findViewById(R.id.zb_btn2);
        zb_view1=findViewById(R.id.zb_view1);
        zb_view2=findViewById(R.id.zb_view2);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        zb_btn1.setOnClickListener(this);
        zb_btn2.setOnClickListener(this);

        List<P_zhangben> zblist= DataSupport.select("zbname").find(P_zhangben.class);
        zbnames=new String[zblist.size()];
        for (int i=0;i<zblist.size();i++){
            zbnames[i]=zblist.get(i).getZbname();
            //Log.i("zhangbenxxx",zbnames[i]);
        }
        zb_sp.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,zbnames));
        zb_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zbname = zbnames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zb_btn1:
                zb_btn1.setBackgroundColor(zb_btn1.getResources().getColor(R.color.common_blue));
                zb_btn2.setBackgroundColor(zb_btn2.getResources().getColor(R.color.common_white));
                zb_btn1.setTextColor(zb_btn1.getResources().getColor(R.color.common_white));
                zb_btn2.setTextColor(zb_btn2.getResources().getColor(R.color.common_dark));
                zb_view1.setVisibility(View.VISIBLE);
                zb_view2.setVisibility(View.GONE);
                status=0;
                break;
            case R.id.zb_btn2:
                zb_btn2.setBackgroundColor(zb_btn2.getResources().getColor(R.color.common_blue));
                zb_btn1.setBackgroundColor(zb_btn1.getResources().getColor(R.color.common_white));
                zb_btn2.setTextColor(zb_btn2.getResources().getColor(R.color.common_white));
                zb_btn1.setTextColor(zb_btn1.getResources().getColor(R.color.common_dark));
                zb_view2.setVisibility(View.VISIBLE);
                zb_view1.setVisibility(View.GONE);
                status=1;
                break;
            case R.id.zb_ok:
                if (status==0){
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("user", zbname);
                    editor.commit();
                    Intent intent1=new Intent(this,MainActivity.class);
                    this.finish();
                    startActivity(intent1);
                }else {
                    P_zhangben pp=new P_zhangben();
                    String nn=zb_add.getText().toString().trim();
                    pp.setZbname(nn);
                    pp.save();
                    SharedPreferences.Editor editor1=sp.edit();
                    editor1.putString("user", nn);
                    editor1.commit();
                    Intent intent2=new Intent(this,MainActivity.class);
                    this.finish();
                    startActivity(intent2);
                }
                break;
            case R.id.zb_cancel:
                Intent intent3=new Intent(this,MainActivity.class);
                this.finish();
                startActivity(intent3);
                break;
        }
    }
    private void initSelectColor() {
        zb_btn1.setBackgroundColor(zb_btn1.getResources().getColor(R.color.common_blue));
        zb_btn2.setBackgroundColor(zb_btn1.getResources().getColor(R.color.common_white));
        zb_btn1.setTextColor(zb_btn1.getResources().getColor(R.color.common_white));
        zb_btn2.setTextColor(zb_btn2.getResources().getColor(R.color.common_dark));
    }
}
