package com.yiweiyihangft.datamonitor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiweiyihangft.datamonitor.Constants;
import com.yiweiyihangft.datamonitor.R;

import java.util.ArrayList;

/**
 * 单个测点显示格式的Adapter
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public Context context;
    /**
     * LOG标记
     */
    private String LOG_TAG = "MyAdapter";
    /**
     * 测点信息表格(切分)
     */
    private ArrayList<String[]> table;
    /**
     * 测点描述列表(未切分)  包含名称和单位 eg: 3.2MPa蒸汽流量(t/h)
     */
    private String[] data;
    /**
     * 测点名称
     */
    private TextView title;
    /**
     * 测点值
     */
    private TextView value;
    /**
     * 测点单位
     */
    private TextView unit;
    /**
     * 滑动条
     */
    private ImageView drag_image;

    /**
     * 构造函数
     */
    public MyAdapter() {
        this.context = Constants.context;
        this.mInflater = LayoutInflater.from(this.context);//动态载入界面
    }

    /**
     * 初始化Adapter要显示的信息
     *
     * @param table 切分后的测点列表
     * @param data  测点描述列表(未切分)  包含名称和单位 eg: 3.2MPa蒸汽流量(t/h)
     * @param proid 工序ID
     */
    public void setData(ArrayList<String[]> table, String[] data, int proid) {
        this.table = table;  // table 切分后测点列表
        this.data = data;   // 测点信息列表 每一条都未切分
        // System.out.println("table.size = "+table.size());
    }


    /**
     * 获得测点总数
     */
    @Override
    public int getCount() {
        return table.size();
    }

    /**
     * 获得用户选择测点的未切分测点信息描述
     *
     * @param position 用户选择的位置
     * @return eg: 3.2MPa蒸汽流量(t/h)
     */
    @Override
    public String getItem(int position) {
        return data[position];
    }

    /**
     * 获得用户点击位置的测点索引值
     *
     * @param position 用户点击位置索引
     * @return
     */
    @Override
    public long getItemId(int position) {
        return (position + 1);
    }

    /**
     * 初始化每条测点的显示方案
     *
     * @param position    用户点击位置索引
     * @param contentView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {

        if (contentView == null) {
            contentView = mInflater.inflate(R.layout.list_item, null);
        }
        // 获得每条测点信息的 测点描述 测点值 单位
        String[] dataEachLine = table.get(position);
        // 绑定页面布局
        title = (TextView) contentView.findViewById(R.id.paradesc);
        value = (TextView) contentView.findViewById(R.id.value);
        unit = (TextView) contentView.findViewById(R.id.unit);
        drag_image = (ImageView) contentView.findViewById(R.id.drag_handle);
        // 设置背景显示
        if (position % 2 == 0) {//奇偶行背景色
            contentView.setBackgroundColor(Color.parseColor("#EBEBEB"));
        } else {
            contentView.setBackgroundColor(Color.parseColor("#E3F2FD"));
        }
        // 向页面填充数据
        title.setText(dataEachLine[0]);
//        title.setBackgroundColor(Color.GRAY);
//        title.setTextColor(Color.RED);
        value.setText(dataEachLine[1]);
//        value.setBackgroundColor(Color.YELLOW);
//        value.setTextColor(Color.WHITE);
        unit.setText(dataEachLine[2]);
//        unit.setTextColor(Color.BLUE);
//        unit.setBackgroundColor(Color.GRAY);
        return contentView;
    }
}

