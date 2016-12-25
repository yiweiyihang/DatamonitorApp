package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.json.JSONObject;

import netRequest.BaseNetTopBusiness;
import netRequest.HttpResponse;
import netRequest.NetTopListener;
import netRequest.ThirdRequest;

/**
 * Created by 32618 on 2016/12/24.
 * 显示对应工序可选择的测点对话框
 */
public class Dialog{
    private Context context;
    private View view;
    public Dialog(Context context,View view){
        this.context = context;
        this.view = view;
    }
    public String[] paraItems;
    private PopupWindow stationSelectDialog;

    /*
       @param: 工序ID
     */

    public void getDialog(final int i){

        // ThirdRequest  申请获得工序对应的测点信息
        ThirdRequest arequest = new ThirdRequest();
        arequest.proID = Integer.toString(i);  //传入id对应工序id
        BaseNetTopBusiness baseNetTopBusiness=new BaseNetTopBusiness(new NetTopListener(){
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                byte[] bytes=response.bytes;
                String s=new String(bytes);
                System.out.println("result="+bytes.length);
                try {
                    String str = new String(bytes, "gbk");
                    System.out.println(str);
                    System.out.println(str.length());
                    JSONObject object = new JSONObject(str);
                    System.out.println("======:" + object.length());
                    // 获取测点项目字符串
                    paraItems = new String[object.length()];
                    for (int k = 1; k <= object.length(); k++) {
                        // 读取JSON解析的测点名称存入字符串
                        paraItems[k - 1] = object.getString(Integer.toString(k));
                    }

                    showMutiChoiceDialog(paraItems,i);
                }
                catch (Exception e)
                {
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

    /*
     显示多项选择对话框
     @param: 单个工序对应的测点字符串，对应的工序ID
     */
    private void showMutiChoiceDialog(final String[] stationsMean,final int i ){
        if(stationSelectDialog == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            // 利用CustomMultipleChoiceView 填充
            View view = inflater.inflate(R.layout.dialog_multiplechoice, null);
            CustomMultipleChoiceView mutipleChoiceView = (CustomMultipleChoiceView) view.findViewById(R.id.CustomMultipleChoiceView);
            // 参数 String[] data, boolean[] isSelected
            mutipleChoiceView.setData(stationsMean, null);
            // 默认情况全选
            mutipleChoiceView.selectAll();
            mutipleChoiceView.setTitle("多选");
            stationSelectDialog = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // 存储用户选择的测点设置到Constants.paramp中
            mutipleChoiceView.setOnSelectedListener(new CustomMultipleChoiceView.onSelectedListener() {
                @Override
                public void onSelected(Boolean[] sparseBooleanArray) {
                    stationSelectDialog.dismiss();
                    for (int j = 0; j < sparseBooleanArray.length; j++) {
                        if (sparseBooleanArray[j]) {
                            Constants.paramap.put(j+1, stationsMean[j]);
                        }
                    }
                    Constants.promap.put(i, Constants.paramap);
//                    Constants.paramap = new LinkedHashMap<Integer, String>();
                }
            });
        }
        // 设置PopupWindow的显示位置
        stationSelectDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
