package com.yiweiyihangft.datamonitor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiweiyihangft.datamonitor.Constants;
import com.yiweiyihangft.datamonitor.R;
import com.yiweiyihangft.datamonitor.utils.GetParaId;

import java.util.List;

/**
 * Created by 32618 on 2016/12/25.
 *  用户选择的工序页面的详细测点信息单条显示Adapter
 *
 */

public class MyAdapter extends BaseAdapter {
    private List<String[]> table;
    private LayoutInflater mInflater;
    public Context context;
    private String[] data;
    private int proid;
    private TextView title ;
    private TextView value ;
    private TextView unit ;

    public MyAdapter( ) {
        this.context= Constants.context;
        this.mInflater = LayoutInflater.from(this.context);//动态载入界面
    }

    @Override
    public int getCount() {
        return table.size();
    }
    @Override
    public Object getItem(int position) {
        if(position==0){
            return null;
        }
        return data[position-1];
    }

    @Override
    public long getItemId(int position) {
        if(position==0){
            return 0;
        }
        GetParaId getParaId = new GetParaId();
        return getParaId.getId(proid,data[position-1]);
    }
    public void setData(List<String[]> data,String[] data1,int proid){
        this.table=data;
        this.data = data1;
        this.proid = proid;
        // System.out.println("table.size = "+table.size());
    }
    @Override
    public View getView(int position, View contentView, ViewGroup parent){

        if (contentView==null) {
            contentView = mInflater.inflate(R.layout.list_item,null);
        }
        String[] data = table.get(position);
        title = (TextView) contentView.findViewById(R.id.paradesc);
        value = (TextView) contentView.findViewById(R.id.value);
        unit = (TextView) contentView.findViewById(R.id.unit);

        if (position == 0) {  //标题行居中
            contentView.setBackgroundColor(Color.parseColor("#A0DDF6"));
        } else if (position % 2 == 0) {//奇偶行背景色
            contentView.setBackgroundColor(Color.parseColor("#AFAFAF"));
        }else {
            contentView.setBackgroundColor(Color.parseColor("#FCFCFC"));
        }
        title.setText(data[0]);
        title.setTextSize(18);
        value.setText(data[1]);
        value.setTextSize(18);
        unit.setText(data[2]);
        unit.setTextSize(16);
        return contentView;
    }
}