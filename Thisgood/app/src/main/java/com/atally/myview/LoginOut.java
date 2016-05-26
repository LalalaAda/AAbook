package com.atally.myview;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.atally.models.P_consume;
import com.atally.models.P_income;
import com.atally.thisgood.LoginActivity;
import com.atally.thisgood.R;

import org.litepal.crud.DataSupport;
//注销弹出框 警告框
public class LoginOut extends DialogFragment {
    private Button ok;
    private Button cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=inflater.inflate(R.layout.loginout_dialog,container);
        ok=(Button)view.findViewById(R.id.loginout_ok);
        cancel=(Button)view.findViewById(R.id.loginout_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();//清空所有本地首选项数据
                //清空所有收支数据
                DataSupport.deleteAll(P_income.class);
                DataSupport.deleteAll(P_consume.class);
                Intent ina=new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(ina);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
