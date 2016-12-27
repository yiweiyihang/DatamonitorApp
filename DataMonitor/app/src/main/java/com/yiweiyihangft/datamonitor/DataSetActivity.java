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

    final String LOG_TAG = "DataSetActivity";
    private int[] countClick;    // 每个工序被点选的次数
    private String[] proItems;   // 所有工序名称组成的数组
    private Dialog dialog ;      // 选择工序名称后显示的对话框
    CheckBox checkBox;
    OnClick on=new OnClick();
    private View rootView;
    private Button setConfirm_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_data_set, null);
        setContentView(rootView);
        setConfirm_bt = (Button)findViewById(R.id.set_confirm_bt);
        sendRequest();   // 发送读取工序申请  显示所有可供选择的工序

        /*
         监听配置完成按钮
         */
        setConfirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.proItems = proItems;   // 存取所有工序名称
                if(Constants.proChoose!=null&&countClick!=null) {
                    // 清空缓存
                    Constants.proChoose.clear();
                    for (int i = 0; i < countClick.length; i++) {
                        //统计被选择的工序
                        // 如被选中 存储用户选择的工序名称
                        // 如未被选中 清空本地存储的工序名称
                        if (countClick[i] % 2 != 0) {
                            //System.out.println(proItems[i]);
                            Constants.proChoose.add(proItems[i]);
                        }else {
                            if (Constants.promap.get(i)!=null){    // 如果对应的i中工序名称不为空
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
                }else{
                    Toast.makeText(DataSetActivity.this,"您还没有选择工序!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
      发送读取工序申请  显示所有可供选择的工序
     */
    private void sendRequest(){
        // SecondRequest 申请得到所有的工序信息
        SecondRequest arequest = new SecondRequest();
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.checkboxID);
                ll.removeAllViews();
                byte[] bytes = response.bytes;
                try {
                    String str = new String(bytes, "gbk");
                    JSONObject object = new JSONObject(str);
                    countClick = new int[object.length()];
                    proItems = new String[object.length()];
                    for (int i = 0; i < object.length(); i++) {
                        //动态添加到已有布局
                        checkBox = new CheckBox(DataSetActivity.this);
                        String s = object.getString(Integer.toString(i));
                        proItems[i] = s;
                        // 设置工序显示的字体 宽度等
                        checkBox.setText(s);
                        checkBox.setTextSize(20);
//                        checkBox.setTextColor(getResources().getColor(R.color.TextColor));
                        checkBox.setId(i);
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


    //监听选择的工序  用户选择工序后显示对话框
    class  OnClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            countClick[v.getId()]++;//记录各个工序点击次数
            Log.v(LOG_TAG,Integer.toString(countClick.length));
            System.out.println(v.getId());
            System.out.println(countClick[v.getId()]);
            if(countClick[v.getId()]%2!=0){//点击次数为奇数则弹出对话框
                // System.out.println("测点");
                dialog = new Dialog(DataSetActivity.this,rootView);
                dialog.getDialog(v.getId());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.proItems = proItems;
    }
}
