package com.atally.thisgood;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.atally.myview.Mylogin;
import com.atally.myview.Myregister;
import com.atally.myview.Myxinxi;

import org.litepal.tablemanager.Connector;

import cn.bmob.v3.Bmob;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thelo);

        //初始化BmobSDK
        Bmob.initialize(this,"749afe6d673147af86dc6e7ca352a848");
        //Log.i("LoginActivity","okokoko");

        //初始化本地数据库
        SQLiteDatabase db = Connector.getDatabase();

        FragmentTransaction ft=getFragmentManager().beginTransaction();
        //获取本地存储的字段，用于判断是否已登录 未登录为0 已登录为1  注册为2 修改个人信息为3
        SharedPreferences sp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        int a_login=sp.getInt("login",0);
        if (a_login==0){
            ft.add(R.id.thelo,new Mylogin()).commit();
        }else if(a_login==1){
            Intent ina=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(ina);
            LoginActivity.this.finish();
        }else if (a_login==2){
            ft.replace(R.id.thelo,new Myregister()).commit();
        }else if (a_login==3){
            ft.replace(R.id.thelo,new Myxinxi()).commit();
        }
    }

}
