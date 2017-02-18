package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.content.SharedPreferences;
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

//    /**
//     * 测点被选择状态Map
//     */
//    public static boolean[] isParaSelected;

    /**
     * 测点名称列表窗口
     */
    private PopupWindow stationSelectDialog;
    /**
     * 存储测点被选择状态
     */
    private SharedPreferences paraSelectedPrf;



    /**
     * 构造函数
     * @param context
     * @param view
     */
    public Dialog(Context context,View view){
        this.context = context;
        this.view = view;
        this.paraSelectedPrf = context.getSharedPreferences("MyProject",Context.MODE_PRIVATE);
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
                    // 新建测点名称数组
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
     * @param proID     对应工序ID
     */
    private void showMutiChoiceDialog(final String[] stationsMean,final int proID ){
        // 初始化窗口
        if(stationSelectDialog == null){
            // 动态生成页面
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_multiplechoice, null);
            // 绑定多选框页面
            CustomMultipleChoiceView mutipleChoiceView = (CustomMultipleChoiceView) view.findViewById(R.id.CustomMultipleChoiceView);
            // 设置对应工序ID
            mutipleChoiceView.setProID(proID);
            // 显示测点名称列表
            mutipleChoiceView.setData(stationsMean,null);
            // TODO 初始全选
//            mutipleChoiceView.selectAll();
            // 设置对话框标题
            mutipleChoiceView.setTitle("多选");

            // 新建窗口对象
            stationSelectDialog = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            // 监听多选框 的选择
            mutipleChoiceView.setOnSelectedListener(new onSelectedListener() {
                @Override
                public void onSelected(Boolean[] sparseBooleanArray) {
                    // 清除缓存
                    stationSelectDialog.dismiss();
                    // 存储用户测点选择状态
                    SharedPreferences.Editor paraSelected = paraSelectedPrf.edit();
                    for (int j = 0; j < sparseBooleanArray.length; j++) {
                        if (sparseBooleanArray[j]) {
                            // 向数据源存储用户选择测点 1序 (测点ID，测点描述)
                            Constants.paramap.put(j+1, stationsMean[j]);
                            paraSelected.putBoolean(proID + "isParaSelected" + (j+1),true);
                            paraSelected.commit();
                        }
                        else{
                            paraSelected.putBoolean(proID + "isParaSelected" + (j+1),false);
                            paraSelected.commit();
                        }
                    }
                    // 向数据源存储工序Map
                    Constants.promap.put(proID, Constants.paramap);
                    // 申请新的存储空间
                    Constants.paramap = new LinkedHashMap<Integer, String>();
                }
            });
        }
        // 设置窗口显示位置
        stationSelectDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
