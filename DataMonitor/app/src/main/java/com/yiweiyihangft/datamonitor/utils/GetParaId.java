package com.yiweiyihangft.datamonitor.utils;

import com.yiweiyihangft.datamonitor.Constants;

import java.util.Map;

/**
 * 获取测点对应ID
 * Created by 32618 on 2016/12/29.
 */
public class GetParaId {
    /**
     * 获取测点ID
     * @param proId   工序ID
     * @param str   测点描述字符串 eg:3.2MPa蒸汽流量(t/h)
     * @return   返回测点ID
     */
    public static int getId(int proId,String str){
        if(Constants.promap!=null) {
            //System.out.println(Constants.promap.get(proId));
            for (Map.Entry<Integer, String> entry :Constants.promap.get(proId).entrySet()) {
                if(str.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
            return -1;
        }
        return -1;
    }
}
