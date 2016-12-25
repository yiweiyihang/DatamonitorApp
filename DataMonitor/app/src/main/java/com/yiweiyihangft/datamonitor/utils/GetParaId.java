package com.yiweiyihangft.datamonitor.utils;

import com.yiweiyihangft.datamonitor.Constants;

import java.util.Map;

/**
 * Created by 32618 on 2016/12/25.
 * 根据工序ID 和测点名称 返回测点ID
 */

public class GetParaId {
    public int getId(int proId,String str){
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
