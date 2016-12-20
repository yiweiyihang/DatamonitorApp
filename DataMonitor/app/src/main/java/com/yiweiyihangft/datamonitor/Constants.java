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
    public static String Url="";
    public static String[] proItems;//从数据库获取的所有工序集合
    public static List<String> proChoose = new ArrayList<>();//已选的工序集合
    public static Map<Integer,Map<Integer,String>> promap = new HashMap<>();//保存用户勾选信息
    public static Map<Integer,String> paramap = new LinkedHashMap<>();
    public static String UserName;
    public static String frequency = "10000";
    public static JSONObject dataObject;
    public static Map<Integer,String> timemap = new HashMap<>();
    public static Map<Integer,JSONObject> alldata = new LinkedHashMap<Integer,JSONObject>();;
    public static Context context;
    public static JSONObject historydata;
}