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
import com.yiweiyihangft.datamonitor.utils.GetParaId;

import java.util.ArrayList;

/**
 * 单个测点显示格式的Adapter
 *
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList<String[]> table;
    private LayoutInflater mInflater;
    public Context context;
    private String[] data;
    private int proid;
    private TextView title ;
    private TextView value ;
    private TextView unit ;
    private ImageView drag_image;


    public MyAdapter( ) {
        this.context= Constants.context;
        this.mInflater = LayoutInflater.from(this.context);//动态载入界面
    }

    @Override
    public int getCount() {
        return table.size();
    }

    @Override
    public String getItem(int position) {
        if(position==0){
            return null;
        }
        return data[position-1];
    }

    public String[] getItemApart(int position){
        if(position == 0){
            return null;
        }
        return table.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position==0){
            return 0;
        }
        GetParaId getParaId = new GetParaId();
        return getParaId.getId(proid,data[position-1]);
    }
    public void setData(ArrayList<String[]> data,String[] data1,int proid){
        this.table=data;  // table 切分后带标题的测点列表
        this.data = data1;   // 测点信息列表 每一条都未切分
        this.proid = proid;
        // System.out.println("table.size = "+table.size());
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){

        if (contentView==null) {
            contentView = mInflater.inflate(R.layout.list_item,null);
        }
        String[] data = table.get(position);   // 每条测点信息的 测点描述 测点值 单位
        title = (TextView) contentView.findViewById(R.id.paradesc);
        value = (TextView) contentView.findViewById(R.id.value);
        unit = (TextView) contentView.findViewById(R.id.unit);
        drag_image = (ImageView)contentView.findViewById(R.id.drag_handle);

        if (position % 2 == 0) {//奇偶行背景色
            contentView.setBackgroundColor(Color.parseColor("#EBEBEB"));
        }else {
            contentView.setBackgroundColor(Color.parseColor("#E3F2FD"));
        }
        title.setText(data[0]);
//        title.setBackgroundColor(Color.GRAY);
//        title.setTextColor(Color.RED);
        value.setText(data[1]);
//        value.setBackgroundColor(Color.YELLOW);
//        value.setTextColor(Color.WHITE);
        unit.setText(data[2]);
//        unit.setTextColor(Color.BLUE);
//        unit.setBackgroundColor(Color.GRAY);
        return contentView;
    }


    public void remove(int arg0) {//删除指定位置的item
        table.remove(arg0);

//        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(String[] item, int arg0) { //在指定位置插入item
        table.add(arg0, item);
//        this.notifyDataSetChanged();
    }
}

