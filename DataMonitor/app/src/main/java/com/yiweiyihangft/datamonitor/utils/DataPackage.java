package com.yiweiyihangft.datamonitor.utils;

import com.google.gson.JsonObject;
import com.yiweiyihangft.datamonitor.Constants;

/**
 * Created by 32618 on 2016/12/29.
 */

public class DataPackage {
    public static JsonObject getReqdata(int proId){
        JsonObject object = new JsonObject();
        GetProId getProId = new GetProId();
        ProChooseed pc = new ProChooseed();
        for(int i = 0; i< Constants.proChoose.size(); i++) {
            String str = Constants.proChoose.get(i);
            int proid = getProId.getId(str);
            JsonObject obj = pc.getParaIdJson(proid);
            object.add(Integer.toString(proid),obj);
        }
        return object;
    }
}