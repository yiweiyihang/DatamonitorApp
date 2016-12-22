package com.yiweiyihangft.datamonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    private TextView dateShow;
    private Button mUserSetBt;
    private Button mDataMonitorBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    /*
     初始化欢迎界面
     */
    private void initView(){
        dateShow = (TextView)findViewById(R.id.data_show_text);
        mUserSetBt = (Button)findViewById(R.id.userSet_bt);
        mDataMonitorBt = (Button)findViewById(R.id.dataMonitor_bt);

        dateShow();     // 显示当前日期
        /*
         监听用户设置按钮
         */
        mUserSetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent userSet = new()
                Toast.makeText(WelcomeActivity.this,"用户设置！",Toast.LENGTH_SHORT).show();
            }
        });

        /*
          监听数据监测按钮
         */
        mDataMonitorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent dataMonitor = new();
                Toast.makeText(WelcomeActivity.this,"数据监测！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String dateShow(){
        return "setDate";
    }
}
