package com.yiweiyihangft.datamonitor.utils;

/**
 * 切分测点信息 得到测点名称和测点单位
 * 参数：eg——3.2MPa蒸汽流量(t/h)
 * 返回：3.2MPa蒸汽流量、t/h
 */

public class GetSubString {
    /**
     * 获得测点名称描述
     * @param str  对应测点信息字符串 eg: 3.2MPa蒸汽流量(t/h)
     * @return  测点名称字符串
     */
    public static String getParadesc(String str){
        if(str!=null) {
            if(str.contains("(")) {
                String[] ss = str.split("\\(");
                if (ss[0] != null) {
                    // 返回测点名称
                    return ss[0];
                }
            }else {return str;}
        }
        return null;
    }

    /**
     * 获得测点单位
     * @param str  测点信息字符串 eg: 3.2MPa蒸汽流量(t/h)
     * @return  测点单位字符串
     */
    public static String getParaunit(String str){
        if(str!=null) {
            if(str.contains("(")) {
                String[] ss = str.split("\\(");
                if (ss[1] != null) {
                    return ss[1].substring(0, ss[1].length() - 1);
                }
            }else {return null;}
        }
        return null;
    }
}