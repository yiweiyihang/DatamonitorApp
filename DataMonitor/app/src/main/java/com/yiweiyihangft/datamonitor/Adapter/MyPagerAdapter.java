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
 * Created by 32618 on 2016/12/29.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private int count;
    private Fragment[] fragments;
    public MyPagerAdapter(int count, List prolist, FragmentManager fm) {
        super(fm);
        this.count=count;
        fragments = null;
        //System.out.println("*********dasfaefsdafa*****"+count);
        GetProId mGetProID = new GetProId();
        ProChooseed mProChooseed = new ProChooseed();
        fragments = new ListViewFragment[count];
        for(int i=0;i<count;i++) {//动态生成每个工序的测点表格
            int proId = mGetProID .getId((String) prolist.get(i));
            //System.out.println(proId);
            String[] paras = mProChooseed.getPara(proId);
            Bundle b = new Bundle();
            b.putStringArray("paralist", paras);
            b.putInt("proId", proId);
            fragments[i] = new ListViewFragment();
            fragments[i].setArguments(b);
        }
    }
    private List titles = Constants.proChoose;
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