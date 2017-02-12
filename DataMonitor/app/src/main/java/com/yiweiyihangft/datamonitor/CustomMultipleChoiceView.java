package com.yiweiyihangft.datamonitor;

/**
 * Created by 32618 on 2016/12/24.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yiweiyihangft.datamonitor.Adapter.MutipleChoiceAdapter;
import com.yiweiyihangft.datamonitor.Adapter.MutipleChoiceAdapter.ViewHolder;

import java.util.ArrayList;

/**
 * 自定义的带 全选/反选 功能的多选对话框
 * 显示用户选择的工序对应的可观测的测点信息  可多选
 *
 */
public class CustomMultipleChoiceView extends LinearLayout {

    /**
     * 测点多选适配器
     */
    private MutipleChoiceAdapter mAdapter;
    /**
     *  测点描述列表
     */
    private String[] data;
    /**
     * 多选窗口标题
     */
    private TextView title;
    private ListView lv;
    /**
     * 确定选择监听器
     */
    private onSelectedListener onSelectedListener;
    /**
     * 当前是否全选
     */
    private boolean curWillCheckAll = false;

    public CustomMultipleChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomMultipleChoiceView(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView(){
		// 关联页面元素
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.custom_mutiplechoice_view, null);
        lv = (ListView) view.findViewById(R.id.mutiplechoice_listview);
        Button bt_selectall = (Button) view.findViewById(R.id.mutiplechoice_selectall_btn);
        Button bt_ok = (Button) view.findViewById(R.id.mutiplechoice_ok_btn);
        title = (TextView) view.findViewById(R.id.mutiplechoice_title);

        if(curWillCheckAll){
            bt_selectall.setText("选");
        }else{
            bt_selectall.setText("反选");
        }
        MyClickListener l = new MyClickListener();

        // 全选按钮的回调接口
        bt_selectall.setOnClickListener(l);
        bt_ok.setOnClickListener(l);

        // 绑定listView的监听器
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ViewHolder holder = (ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
//                Toast.makeText(getContext(), "选中这个！", Toast.LENGTH_SHORT).show();
                // 将CheckBox的选中状况记录下来
                mAdapter.getIsSelected()[position]=holder.cb.isChecked();
            }
        });
        addView(view);
    }

    /**
     * 用户选择的测点显示设置 checkbox勾选
     * @param data              测点描述列表  测点名+单位
     * @param isSelected        测点被选择状态标志
     */
    public void setData(String[] data, ArrayList<Boolean> isSelected){
        if(data == null){
            throw new IllegalArgumentException("data is null");
        }
        this.data = data;
        mAdapter = new MutipleChoiceAdapter(data, getContext());
        if(isSelected != null){
            if(isSelected.size() != data.length){
                throw new IllegalArgumentException("data's length not equal the isSelected's length");
            }else{
                for(int i=0; i<isSelected.size(); i++){
                    mAdapter.getIsSelected()[i]=isSelected.get(i);
                }
            }
        }
        // 绑定Adapter
        lv.setAdapter(mAdapter);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        if(this.title != null){
            this.title.setText(title);
        }
    }

    /**
     * 设置选择监听器
     * @param l
     */
    public void setOnSelectedListener(onSelectedListener l){
        this.onSelectedListener = l;
    }

    public interface onSelectedListener{
        public void onSelected(Boolean[] sparseBooleanArray);
    }


    /**
     * 全选
     */
    public void selectAll(){
        if(data != null){
            for (int i = 0; i < data.length; i++) {
                mAdapter.getIsSelected()[i] =true;
            }
            // 刷新listview和TextView的显示
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 全不选
     */
    public void deselectAll(){
        if(data != null){
            for (int i = 0; i < data.length; i++) {
                mAdapter.getIsSelected()[i] = false;
            }
            // 刷新listview和TextView的显示
            mAdapter.notifyDataSetChanged();
        }
    }


    private class MyClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mutiplechoice_selectall_btn:
                    //全选/反选按钮
                    if(data == null){
                        return;
                    }
                    if(curWillCheckAll){
                        selectAll();
                    }else{
                        deselectAll();
                    }
                    if(curWillCheckAll){
                        ((Button)v).setText("反选");
                    }else{
                        ((Button)v).setText("全选");
                    }
                    curWillCheckAll = !curWillCheckAll;
                    break;
                case R.id.mutiplechoice_ok_btn:
                    //确定选择的按钮
                    if(onSelectedListener != null && mAdapter != null){
                        onSelectedListener.onSelected(mAdapter.getIsSelected());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}