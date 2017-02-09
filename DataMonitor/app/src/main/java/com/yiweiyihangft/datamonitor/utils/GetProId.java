package com.yiweiyihangft.datamonitor.utils;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * 获取工序对应ID
 * Created by 32618 on 2016/12/29.
 */

public class GetProId {
    public static int getId(String str){
        for(int i = 0; i< Constants.proItems.length; i++){
            if(Constants.proItems[i].equals(str)){
                System.out.println("id="+i);
                return i;
            }
        }
        return -1;
    }
}