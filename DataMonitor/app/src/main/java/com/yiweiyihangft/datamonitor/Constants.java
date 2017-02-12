package com.yiweiyihangft.datamonitor;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 32618 on 2016/12/20.
 */

public class Constants {
    /**
     * Url(ip + port)
     */
    public static String Url="";
    /**
     * 从数据库获取的工序名称列表
     */
    public static String[] proItems;
    /**
     * 用户已选择工序名列表
     */
    public static List<String> proChoose = new ArrayList<>();//已选的工序名称集合
    /**
     * 工序选择状态
     */
    public static ArrayList<Boolean> isProSelected = new ArrayList<>();

    //Integer：工序ID  Map<Integer,String>  该工序用户要监测的测点
    public static Map<Integer,Map<Integer,String>> promap = new HashMap<>();

    /**
     * 测点名称Map
     * Integer：测点ID   String：测点名称
     */
    public static Map<Integer,String> paramap = new LinkedHashMap<>();
    /**
     * 测点选择状态Map
     * Integer: 测点ID Boolean：测点被选择状态
     */
    public static ArrayList<Boolean> isParaSelected = new ArrayList<>() ;
    public static String UserName;
    public static String frequency = "15";

    public static JSONObject dataObject;   //存取返回的数据json 对象

    // Integer：工序ID  String 测点时间
    public static Map<Integer,String> timemap = new HashMap<>();

    // Integer：工序ID   JSONObject：返回的测点json数据
    public static Map<Integer,JSONObject> alldata = new LinkedHashMap<Integer,JSONObject>();;


    public static Context context;

    // 测点历史json数据
    public static JSONObject historydata;
}