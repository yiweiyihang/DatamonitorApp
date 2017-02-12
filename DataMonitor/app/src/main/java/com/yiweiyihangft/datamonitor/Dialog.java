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
 * 显示对应工序可选择的测点列表
 */
public class Dialog{
    private Context context;
    private View view;
    /**
     * 测点名称数组
     */
    public String[] paraItems;

    /**
     * 测点被选择状态Map
     */
    public static boolean[] isParaSelected;
    /**
     * 测点名称窗口
     */
    private PopupWindow stationSelectDialog;

    /**
     * 构造函数
     * @param context
     * @param view
     */
    public Dialog(Context context,View view){
        this.context = context;
        this.view = view;
    }

    /**
     * 获取测点名称列表
     * @param proID 工序ID
     */
    public void getDialog(final int proID){
        // 新建请求
        ThirdRequest arequest = new ThirdRequest();
        //传入对应工序id
        arequest.proID = Integer.toString(proID);
        // 发送请求
        BaseNetTopBusiness baseNetTopBusiness=new BaseNetTopBusiness(new NetTopListener(){
            @Override
            public void onSuccess(HttpResponse response) {
                // 获得返回字节流
                byte[] bytes=response.bytes;

                /******* 调试专用 ********/
                System.out.println("成功");
                String s=new String(bytes);
                System.out.println("result="+bytes.length);
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
                    paraItems = new String[object.length()];
                    //根据数据库获取的id与测点名包装的json对象
                    for (int k = 1; k <= object.length(); k++) {
                        // 获得测点名称    注意测点ID是 1序 的   测点名称数组是0序的
                        paraItems[k - 1] = object.getString(Integer.toString(k));
                    }
                    // 显示测点多选对话框
                    showMutiChoiceDialog(paraItems,proID);
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

    /**
     * 显示测点多选对话框
     * @param stationsMean 测点名称列表
     * @param i     对应工序ID
     */
    private void showMutiChoiceDialog(final String[] stationsMean,final int i ){
        // 初始化窗口
        if(stationSelectDialog == null){
            // 动态生成页面
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_multiplechoice, null);
            // 绑定多选框页面
            CustomMultipleChoiceView mutipleChoiceView = (CustomMultipleChoiceView) view.findViewById(R.id.CustomMultipleChoiceView);
            // 显示测点名称列表
            mutipleChoiceView.setData(stationsMean, isParaSelected);
            // 全选
            mutipleChoiceView.selectAll();
            // 设置对话框标题
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
