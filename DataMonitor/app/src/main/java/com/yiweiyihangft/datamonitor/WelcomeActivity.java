package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户欢迎页面
 */

public class WelcomeActivity extends AppCompatActivity {
    private static final int msgKey1 = 1;
    /**
     * 时间显示
     */
    private TextView mTime;
    /**
     * 用户设置按钮
     */
    private Button mUserSetBt;
    /**
     * 数据监测按钮
     */
    private Button mDataMonitorBt;

    /**
     * 开始监测按钮
     */
    private Button mMonitorStartBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 初始化欢迎页面
        initView();
    }

    /**
     * 初始化欢迎界面
     */
    private void initView() {
        // 关联页面元素
        mTime = (TextView) findViewById(R.id.data_show_text);
        mUserSetBt = (Button) findViewById(R.id.userSet_bt);
        mDataMonitorBt = (Button) findViewById(R.id.dataMonitor_bt);
        mMonitorStartBt = (Button)findViewById(R.id.monitorStart_bt);
        // 获得当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String sysTimeStr = formatter.format(curDate);
        // 显示当前时间
        mTime.setText(sysTimeStr);
        // 时间更新
        new TimeThread().start();
        // 监听用户设置按钮
        mUserSetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 转到用户设置活动
                Intent userSet = new Intent(WelcomeActivity.this,UserSetActivity.class);
                startActivity(userSet);
            }
        });

        // 监听数据监测按钮
        mDataMonitorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 转到 数据监测设置活动
                Intent dataSet = new Intent(WelcomeActivity.this,DataSetActivity.class);
                startActivity(dataSet);
//                Toast.makeText(WelcomeActivity.this, "数据监测！！", Toast.LENGTH_SHORT).show();
            }
        });
        mMonitorStartBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // // 转到 数据监测设置活动
                Intent dataMonitor = new Intent(WelcomeActivity.this,ConnectActivity.class);
                startActivity(dataMonitor);
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
