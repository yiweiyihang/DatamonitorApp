package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import netRequest.BaseNetTopBusiness;
import netRequest.HttpResponse;
import netRequest.NetTopListener;
import netRequest.SecondRequest;

/*
  工序选择设置
 */
public class DataSetActivity extends AppCompatActivity {

    /**
     * 标记
     */
    final String LOG_TAG = "DataSetActivity";
    private View rootView;
    /**
     * 每个工序条被点选的次数
     */
    private int[] countClick;
    /**
     * 工序名称列表
     */
    private String[] proItems;
    /**
     * 工序选择状态
     */
    private boolean[] isProSelected;
    /**
     * 选择工序名称后的测点选择对话框
     */
    private Dialog dialog;
    /**
     * 选框
     */
    CheckBox checkBox;
    /**
     * 选框监听器
     */
    OnClick on = new OnClick();
    /**
     * 工序选择确定按钮
     */
    private Button setConfirm_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 生成页面并绑定
        rootView = View.inflate(this, R.layout.activity_data_set, null);
        setContentView(rootView);
        //关联页面元素
        setConfirm_bt = (Button) findViewById(R.id.set_confirm_bt);
        // 初始化工序显示页面 发送读取工序申请  返回所有可供选择的工序并显示在页面上
        iniView();

        // 监听配置完成按钮
        setConfirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存储所有工序名称
                Constants.proItems = proItems;
                Constants.isProSelected = isProSelected;
                if (Constants.proChoose != null && countClick != null) {
                    // 清空缓存
                    Constants.proChoose.clear();
                    //统计被选择的工序
                    for (int i = 0; i < proItems.length; i++) {
                        // 判断是否该工序是否被选中
                        if (countClick[i] % 2 != 0) {   // 该工序选中
                            // 存储用户选择工序名称
                            Constants.proChoose.add(proItems[i]);
                            // 设置该工序选择状态为true
                            Constants.isProSelected[i] = true;
                        } else {      // 该工序未被选中
                            // 判断该工序是否选择过测点
                            if (Constants.promap.get(i) != null) {
                                // TODO 清除未选择工序的测点Map缓存
                                Constants.promap.remove(i);
                            }
                        }
                    }
                    // 转到ShowDataActivity
                    Intent i = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", Constants.proChoose.size());  // 发送选择工序的个数
                    i.putExtras(bundle);
                    i.setClass(DataSetActivity.this, ShowDataActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(DataSetActivity.this, "您还没有选择工序!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 初始化工序显示你页面
     * 发送读取工序申请  显示所有可供选择的工序
     */
    private void iniView() {
        // SecondRequest 申请得到所有的工序信息
        SecondRequest arequest = new SecondRequest();
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.checkboxID);
                ll.removeAllViews();
                // 获得返回字节
                byte[] bytes = response.bytes;
                // 显示工序列表
                try {
                    String str = new String(bytes, "gbk");
                    JSONObject object = new JSONObject(str);
                    countClick = new int[object.length()];    //工序点选次数数组
                    proItems = new String[object.length()];    // 工序名称数组
                    isProSelected = new boolean[object.length()];  // 工序选择状态数组
                    // 逐条显示工序
                    for (int i = 0; i < object.length(); i++) {
                        // 动态添加到已有布局
                        checkBox = new CheckBox(DataSetActivity.this);
                        // 获取工序名称
                        String s = object.getString(Integer.toString(i));
                        proItems[i] = s;
                        // 设置工序显示的字体 宽度等
                        checkBox.setText(s);
                        checkBox.setTextSize(20);
                        // 显示工序选择状态
                        checkBox.setChecked(isProSelected[i]);
//                        checkBox.setTextColor(getResources().getColor(R.color.TextColor));
                        checkBox.setId(i);
                        // 监听选框点击事件
                        checkBox.setOnClickListener(on);
                        checkBox.setHeight(120);
                        // 动态添加到布局中
                        ll.addView(checkBox);
                    }
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


    //监听选择工序选框  用户选择工序后显示对话框
    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            countClick[v.getId()]++;  //记录各个工序点击次数
            /********** 调试专用 *************/
            Log.v(LOG_TAG, Integer.toString(countClick.length));
            System.out.println(v.getId());
            System.out.println(countClick[v.getId()]);
            /********** 调试专用 *************/
            //点击次数为奇数 弹出测点选择对话框
            if (countClick[v.getId()] % 2 != 0) {
                // 创建测点显示对话框
                dialog = new Dialog(DataSetActivity.this, rootView);
                // 显示工序ID对应的测点列表
                dialog.getDialog(v.getId());
            }
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Constants.proItems = proItems;
//    }
}
