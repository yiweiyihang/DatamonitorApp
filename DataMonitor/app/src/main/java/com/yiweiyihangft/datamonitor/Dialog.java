package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yiweiyihangft.datamonitor.CustomMultipleChoiceView.onSelectedListener;

import org.json.JSONObject;

import java.util.LinkedHashMap;

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
    public void getDialog(final int i){
        ThirdRequest arequest = new ThirdRequest();
        arequest.proID = Integer.toString(i);//传入id对应工序id
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
                    paraItems = new String[object.length()];
                    for (int k = 1; k <= object.length(); k++) {//根据数据库获取的id与测点名包装的json对象
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
    private void showMutiChoiceDialog(final String[] stationsMean,final int i ){
        if(stationSelectDialog == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_multiplechoice, null);
            CustomMultipleChoiceView mutipleChoiceView = (CustomMultipleChoiceView) view.findViewById(R.id.CustomMultipleChoiceView);
            mutipleChoiceView.setData(stationsMean, null);
            System.out.println("station.length-"+stationsMean.length);
            mutipleChoiceView.selectAll();
            mutipleChoiceView.setTitle("多选");
            stationSelectDialog = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            mutipleChoiceView.setOnSelectedListener(new onSelectedListener() {
                @Override
                public void onSelected(Boolean[] sparseBooleanArray) {
                    stationSelectDialog.dismiss();
                    for (int j = 0; j < sparseBooleanArray.length; j++) {
                        if (sparseBooleanArray[j]) {
                            Constants.paramap.put(j+1, stationsMean[j]);
                        }
                    }
                    Constants.promap.put(i, Constants.paramap);
                    Constants.paramap = new LinkedHashMap<Integer, String>();
                }
            });
        }
        stationSelectDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
