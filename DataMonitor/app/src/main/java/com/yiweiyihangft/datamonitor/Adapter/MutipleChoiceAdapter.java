package com.yiweiyihangft.datamonitor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yiweiyihangft.datamonitor.R;

/**
 * Created by 32618 on 2016/12/24.
 * 点选工序后显示的多选测点Dialog的适配器
 */

public class MutipleChoiceAdapter extends BaseAdapter {

    // 填充数据的list
    private String[] list;
    // 用来控制CheckBox的选中状况
    private Boolean[] isSelected;
    // 用来导入布局
    private LayoutInflater inflater;

    public MutipleChoiceAdapter(String[] list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new Boolean[list.length];
        // 初始化数据
        initData();
        System.out.println(list.length);
        for(int i=0;i<list.length;i++)
        {
            System.out.println("**************="+list[i]);
        }
    }

    // 初始化isSelected的数据
    private void initData() {
        for (int i = 0; i < list.length; i++) {
            getIsSelected()[i] = false;
        }
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();

            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.custom_mutiplechoice_view_list_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.tv.setTextSize(16);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv.setText(list[position]);
        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(getIsSelected()[position]);
        return convertView;
    }

    public Boolean[] getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean[] isSelected) {
        this.isSelected = isSelected;
    }

    public static class ViewHolder {
        TextView tv;
        public CheckBox cb;
    }
}