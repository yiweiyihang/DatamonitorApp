package com.yiweiyihangft.datamonitor.utils;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by 32618 on 2017/1/8.
 */

public class LineChartModel {
    String title=null;
    ArrayList<Entry> yValues;
    ArrayList<String> xValues;
    public void setTitle(String title)
    {
        this.title=title;
    }
    public void setxValues(ArrayList<String> xValues)
    {
        this.xValues=xValues;
    }
    public void setyValues(ArrayList<Entry> yValues)
    {
        this.yValues=yValues;
    }
    public String getTitle()
    {
        return title;
    }
    public ArrayList<String> getxValues()
    {
        return xValues;
    }
    public ArrayList<Entry> getyValues()
    {
        return yValues;
    }
}