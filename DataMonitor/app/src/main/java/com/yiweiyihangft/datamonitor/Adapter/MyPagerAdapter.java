package com.yiweiyihangft.datamonitor.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.yiweiyihangft.datamonitor.Constants;
import com.yiweiyihangft.datamonitor.ListViewFragment;
import com.yiweiyihangft.datamonitor.utils.GetProId;
import com.yiweiyihangft.datamonitor.utils.ProChooseed;

import java.util.List;

/**
 * 数据显示页面适配器
 * Created by 32618 on 2016/12/29.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    /**
     * 用户选择工序个数
     */
    private int count;
    /**
     * 工序显示页面
     */
    private Fragment[] fragments;
    /**
     * 工序名称数组
     */
    private List titles = Constants.proChoose;

    /**
     * 构造函数
     * @param count     用户选择工序个数
     * @param prolist   用户选择工序名列表  Constant.proChoose
     * @param fm        Fragment管理器
     */
    public MyPagerAdapter(int count, List prolist, FragmentManager fm) {
        super(fm);
        this.count=count;
        fragments = null;
        //System.out.println("*********dasfaefsdafa*****"+count);


        ProChooseed mProChooseed = new ProChooseed();
        // 建立页面
        fragments = new ListViewFragment[count];
        //动态生成每个工序的测点表格
        for(int i=0;i<count;i++) {

            // 获取工序ID
            int proId = GetProId.getId((String) prolist.get(i));
            // 获得工序ID对应的测点表格
            String[] paras = mProChooseed.getPara(proId);
            // 打包工序的测点表格
            Bundle b = new Bundle();
            b.putStringArray("paralist", paras);
            b.putInt("proId", proId);
            // 将获得的工序的测点表格信息发送给ListViewFragment
            fragments[i] = new ListViewFragment();
            fragments[i].setArguments(b);
        }
    }

    @Override
    public int getCount() {
        if (titles == null) {
            return 0;
        } else {
            return count;
        }
    }
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
    public Fragment getFragment(int position){
        //System.out.println("========f.l="+fragments.length);
        return fragments[position];
    }
}