package com.yiweiyihangft.datamonitor.utils;

/**
 * Created by 32618 on 2016/12/25.
 */

public class GetSubString {
    public String getParadesc(String str){
        if(str!=null) {
            if(str.contains("(")) {
                String[] ss = str.split("\\(");
                if (ss[0] != null) {
                    return ss[0];
                }
            }else {return str;}
        }
        return null;
    }
    public String getParaunit(String str){
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