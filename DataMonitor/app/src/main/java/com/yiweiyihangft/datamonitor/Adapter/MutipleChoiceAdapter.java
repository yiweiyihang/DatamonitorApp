package com.yiweiyihangft.datamonitor.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

    /**
     * 适配器数据
     */
    private String[] list;
    /**
     * 测点选择状态
     */
    private Boolean[] isSelected;


    /**
     * 布局生成器
     */
    private LayoutInflater inflater;
    /**
     * 对应工序ID
     */
    private int proID;
    /**
     * 存储测点被选择状态
     */
    private SharedPreferences paraSelectedPrf;

    public MutipleChoiceAdapter(int proID,String[] list, Context context) {
        this.list = list;
        this.proID = proID;
        inflater = LayoutInflater.from(context);
        paraSelectedPrf = context.getSharedPreferences("MyProject",Context.MODE_PRIVATE);
        isSelected = new Boolean[list.length];

        // 获取用户选择状态
        initParaSelected();

        /********* 调试专用 *************/
        System.out.println(list.length);
        for(int i=0;i<list.length;i++)
        {
            System.out.println("**************="+list[i]);
        }
        /********* 调试专用 *************/
    }


    /**
     * 读取用户测点选择
     */
    private void initParaSelected() {
        for (int i = 0; i < list.length; i++) {
            boolean isSelected = paraSelectedPrf.getBoolean( proID + "isParaSelected" + (i+1),false);
            getIsSelected()[i] = isSelected;
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