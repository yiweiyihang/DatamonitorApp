package com.yiweiyihangft.datamonitor.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yiweiyihangft.datamonitor.Constants;

import java.util.Map;

/**
 * Created by 32618 on 2016/12/29.
 */

public class ProChooseed {
    private String[] proChoose;
    private String[] paraChoose;
    private int i = 0;
    public String[] getPro(){
        if(Constants.promap!=null) {
            for (Map.Entry<Integer, Map<Integer, String>> entry : Constants.promap.entrySet()) {
                proChoose[i] = Constants.proItems[entry.getKey()];
                i++;
            }
            return proChoose;
        }
        return null;
    }
    public String[] getPara(int id) {
        int j = 0;
        if(Constants.promap!=null) {
            //System.out.println(Constants.promap.get(id));
            paraChoose = new String[Constants.promap.get(id).size()];

            for (Map.Entry<Integer, String> entry : Constants.promap.get(id).entrySet()) {
                paraChoose[j] = entry.getValue();
                j++;
            }
            return paraChoose;
        }
        return null;
    }
    public JsonObject getParaIdJson(int proid) {
        if(Constants.promap!=null) {
            //System.out.println(Constants.promap.get(proid));
            //paraIdChoose = new String[Constants.promap.get(id).size()];

            JsonArray array = new JsonArray();
            for (Map.Entry<Integer, String> entry : Constants.promap.get(proid).entrySet()) {
                JsonObject object = new JsonObject();
                object.addProperty("id", entry.getKey());
                array.add(object);
            }
            JsonObject object1 = new JsonObject();
            object1.addProperty("proID",proid);
            object1.add("paralist", array);
            return object1;
        }
        return null;
    }

    public String[] getProChoose(){
        if(Constants.promap!=null) {
            proChoose = new String[Constants.proChoose.size()];
            for (int i = 0; i < Constants.proChoose.size(); i++) {
                proChoose[i] = Constants.proChoose.get(i);
            }
            return proChoose;
        }else{
            return null;
        }
    }
}
