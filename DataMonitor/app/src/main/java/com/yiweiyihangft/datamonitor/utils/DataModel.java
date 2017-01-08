package com.yiweiyihangft.datamonitor.utils;

import com.github.mikephil.charting.data.Entry;
import com.yiweiyihangft.datamonitor.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DataModel {
    private static DataModel dataModel = null;

    public static DataModel getInstance() {
        if (dataModel == null) {
            dataModel = new DataModel();
        }
        return dataModel;
    }
    private DataModel() {

    }
    //生成LineChartModel,分别产生x轴，y轴集合
    public LineChartModel getLineChartModel(int point) {
        JSONObject object = Constants.historydata;

        ArrayList<Entry> entryArrayList = new ArrayList<>();
        for (int i = 1; i <= object.length(); i++) {

            Entry entry = null;
            try {
                entry = new Entry((float) object.getDouble(Integer.toString(i)), i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            entryArrayList.add(entry);
        }
        ArrayList<String> xvalues = new ArrayList<>();
        for (int i = 0; i <= point; i++) {
            xvalues.add(i + "");
        }
        LineChartModel lineChartModel = new LineChartModel();
        lineChartModel.yValues = entryArrayList;
        lineChartModel.xValues = xvalues;
        return lineChartModel;
    }
}