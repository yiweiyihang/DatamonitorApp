package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeActivity extends AppCompatActivity {
    private static final int msgKey1 = 1;
    private TextView mTime;
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
    private void initView() {
        mTime = (TextView) findViewById(R.id.data_show_text);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String sysTimeStr = formatter.format(curDate);
        mTime.setText(sysTimeStr);
        new TimeThread().start();
        mUserSetBt = (Button) findViewById(R.id.userSet_bt);
        mDataMonitorBt = (Button) findViewById(R.id.dataMonitor_bt);

        /*
         监听用户设置按钮
         */
        mUserSetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userSet = new Intent(WelcomeActivity.this,UserSetActivity.class);
                startActivity(userSet);
                Toast.makeText(WelcomeActivity.this, "用户设置！", Toast.LENGTH_SHORT).show();
            }
        });

        /*
          监听数据监测按钮
         */
        mDataMonitorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent dataMonitor = new();
                Toast.makeText(WelcomeActivity.this, "数据监测！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    // 获取当前时间并展示
                    SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
                    Date curDate = new Date(System.currentTimeMillis());
                    String sysTimeStr = formatter.format(curDate);
                    mTime.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(20*1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
