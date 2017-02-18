package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import netRequest.BaseNetTopBusiness;
import netRequest.HttpResponse;
import netRequest.NetTopListener;
import netRequest.SecondRequest;
import netRequest.ThirdRequest;

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
    /**
     * 工序名称列表
     */
    private String[] proItems;
    /**
     * 测点名称数组
     */
    public String[] paraItems;
    /**
     * 工序选择状态
     */
    private SharedPreferences SelectedPrf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 初始化欢迎页面
        initView();

        SelectedPrf = getSharedPreferences("MyProject", MODE_PRIVATE);
        getProItems();
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
                // 转到ShowDataActivity
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                // 发送选择工序的个数
                bundle.putInt("count", Constants.proChoose.size());
                i.putExtras(bundle);
                i.setClass(WelcomeActivity.this, ShowDataActivity.class);
                startActivity(i);
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


    /**
     * 发送读取工序申请  获取所有工序
     */
    private String[] getProItems() {
        // SecondRequest 申请得到所有的工序信息
        SecondRequest arequest = new SecondRequest();
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                // 获得返回字节
                byte[] bytes = response.bytes;
                // 显示工序列表
                try {
                    String str = new String(bytes, "gbk");
                    JSONObject object = new JSONObject(str);
                    proItems = new String[object.length()];    // 工序名称数组
                    // 逐条显示工序
                    for (int i = 0; i < object.length(); i++) {
                        // 动态添加到已有布局
                        // 获取工序名称
                        String s = object.getString(Integer.toString(i));
                        proItems[i] = s;
                    }
                    storeProItems();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {
                System.out.println("on fail");
            }

            @Override
            public void onError() {
                System.out.println("on error");
            }
        });
        baseNetTopBusiness.startRequest(arequest);
        return proItems;
    }

    /**
     * 获取用户选择工序
     */
    private void storeProItems() {
        // 存储所有工序名称
        Constants.proItems = proItems;
        if (Constants.proChoose != null) {
            // 清空缓存
            Constants.proChoose.clear();
            //统计被选择的工序
            // 读取工序选择状态
            for (int i = 0; i < proItems.length; i++) {
                // 判断是否该工序是否被选中
                // 读取工序选择状态
                boolean isSelected = SelectedPrf.getBoolean("isProSelected" + i, false);
                if (isSelected) {   //该工序选中
                    // 存储用户选择工序名称
                    Constants.proChoose.add(proItems[i]);
                    // 存储工序选择状态到数据源中
                    Constants.isProSelected.add(i, true);
                    // 获取测点信息
                    getParam(i);
                    // 存储工序选择状态到SharedPrefer中
                } else {      // 该工序未被选中
                    // 判断该工序是否选择过测点
//                            if (Constants.promap.get(i) != null) {
                    // TODO 清除未选择工序的测点Map缓存
                    Constants.promap.remove(i);
                    // 存储工序选择状态到数据源中
                    Constants.isProSelected.add(i, false);
                }
            }

        } else {
            Toast.makeText(WelcomeActivity.this, "您还没有选择工序!", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 获取测点名称列表
     *
     * @param proID 工序ID
     */
    public void getParam(final int proID) {
        // 新建请求
        ThirdRequest arequest = new ThirdRequest();
        //传入对应工序id
        arequest.proID = Integer.toString(proID);
        // 发送请求
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                // 获得返回字节流
                byte[] bytes = response.bytes;

                /******* 调试专用 ********/
                System.out.println("成功");
                String s = new String(bytes);
                System.out.println("result=" + bytes.length);
                /******* 调试专用 ********/
                // 获得并显示测点名称列表
                try {
                    String str = new String(bytes, "gbk");
                    /******* 调试专用 ********/
                    System.out.println(str);
                    System.out.println(str.length());
                    /******* 调试专用 ********/

                    JSONObject object = new JSONObject(str);
                    System.out.println("======:" + object.length());
                    // 新建测点名称数组
                    paraItems = new String[object.length()];
                    //根据数据库获取的id与测点名包装的json对象
                    for (int k = 1; k <= object.length(); k++) {
                        // 获得测点名称    注意测点ID是 1序 的   测点名称数组是0序的
                        paraItems[k - 1] = object.getString(Integer.toString(k));
                    }
                    // 读取用户测点选择并存储
                    storeParamChoose(paraItems, proID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {
                System.out.println("on fail");
            }

            @Override
            public void onError() {
                System.out.println("on error");
            }
        });
        baseNetTopBusiness.startRequest(arequest);
    }


    /**
     * 存储测点选择
     *
     * @param stationsMean 测点名称列表
     * @param proID        对应工序ID
     */
    private void storeParamChoose(final String[] stationsMean, final int proID) {

        for (int j = 0; j < stationsMean.length; j++) {
            // 读取用户测点选择状态
            boolean isSelected = SelectedPrf.getBoolean(proID + "isParaSelected" + (j + 1), false);
            if (isSelected) {
                // 向数据源存储用户选择测点 1序 (测点ID，测点描述)
                Constants.paramap.put(j + 1, stationsMean[j]);
            }
        }
        // 向数据源存储工序Map
        Constants.promap.put(proID, Constants.paramap);
        // 申请新的存储空间
        Constants.paramap = new LinkedHashMap<Integer, String>();

    }
}
