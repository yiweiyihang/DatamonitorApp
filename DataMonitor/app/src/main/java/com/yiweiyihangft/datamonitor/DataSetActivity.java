package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import netRequest.BaseNetTopBusiness;
import netRequest.HttpResponse;
import netRequest.NetTopListener;
import netRequest.SecondRequest;

public class DataSetActivity extends AppCompatActivity {

    final String LOG_TAG = "DataSetActivity";
    private int[] countClick;
    private String[] proItems;
    private Dialog dialog ;
    CheckBox checkBox;
//    OnClick on=new OnClick();
    private View rootView;
    private Button setConfirm_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_data_set, null);
        setContentView(rootView);
        setConfirm_bt = (Button)findViewById(R.id.set_confirm_bt);
        sendRequest();
        setConfirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.proItems = proItems;
                if(Constants.proChoose!=null&&countClick!=null) {
                    //System.out.println(countClick.length);
                    Constants.proChoose.clear();
                    for (int i = 0; i < countClick.length; i++) {
                        if (countClick[i] % 2 != 0) {//统计有几个工序被选择
                            //System.out.println(proItems[i]);
                            Constants.proChoose.add(proItems[i]);
                        }else {
                            if (Constants.promap.get(i)!=null){
                                Constants.promap.remove(i);
                            }
                        }
                    }
                    Intent i = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", Constants.proChoose.size());
                    i.putExtras(bundle);
                    i.setClass(DataSetActivity.this, ShowDataActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(DataSetActivity.this,"您还没有选择工序!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendRequest(){
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
                        checkBox.setText(s);
                        checkBox.setId(i);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked){
                                    dialog = new Dialog(DataSetActivity.this,rootView);
                                    dialog.getDialog(checkBox.getId());
                                }
                            }
                        });
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

    //监听选择的工序
//    class  OnClick implements View.OnClickListener
//    {
//        @Override
//        public void onClick(View v) {
//            countClick[v.getId()]++;//记录各个工序点击次数
//            Log.v(LOG_TAG,Integer.toString(countClick.length));
//            System.out.println(v.getId());
//            System.out.println(countClick[v.getId()]);
//            if(countClick[v.getId()]%2!=0){//点击次数为奇数则弹出对话框
//                // System.out.println("测点");
//                dialog = new Dialog(DataSetActivity.this,rootView);
//                dialog.getDialog(v.getId());
//            }
//        }
//    }
}
