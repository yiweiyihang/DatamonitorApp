package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yiweiyihangft.datamonitor.Adapter.MyPagerAdapter;
import com.yiweiyihangft.datamonitor.utils.GetProId;
import com.yiweiyihangft.datamonitor.utils.ProChooseed;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import netRequest.BaseNetTopBusiness;
import netRequest.DataRequest;
import netRequest.HttpResponse;
import netRequest.NetTopListener;


public class ShowDataActivity extends AppCompatActivity {
    final String LOG_TAG = "ShowDataActivity";
    private TextView proName;
    private TextView proTime;
    private static MyPagerAdapter myPagerAdapter;
    private ViewPager pager;
    private int count;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int i;
    private GetProId mGetProId = new GetProId();
    private ProChooseed mProChooseed = new ProChooseed();
    private int proId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Constants.context=this;
        System.out.println("************************---------");
        System.out.print(Constants.proChoose.toString());
        pager = (ViewPager) findViewById(R.id.pagers);
        Intent intent = this.getIntent();
        // 接受用户选择的工序个数
        Bundle bundle=intent.getExtras();
        count=bundle.getInt("count");
        //System.out.println("count=" + count);
        proName = (TextView) findViewById(R.id.proName);
        proTime = (TextView) findViewById(R.id.proTime);
        // 清空缓存
        Constants.alldata.clear();
        Constants.timemap.clear();
//        final GetProId getProId = new GetProId();
//        final ProChooseed pc = new ProChooseed();

        // 对于用户选择的每一道工序进行操作
        for(i=0;i<Constants.proChoose.size();i++) {
            DataRequest dataRequest = new DataRequest();
            //从本地存储中读取用户选择的工序字符串  返回工序对应的ID
            final int proId = mGetProId.getId(Constants.proChoose.get(i));
            System.out.println("======proId="+proId);
            // 包装好请求数据信息包括工序id和测点id  用json包装
            dataRequest.dataReq = mProChooseed.getParaIdJson(proId).toString();
            BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    System.out.println("成功");
                    byte[] bytes = response.bytes;
                    try {
                        String str = new String(bytes, "gbk");
                        Constants.dataObject = new JSONObject(str);
                        //存储工序ID 以及对应的时间
                        Constants.timemap.put(proId,Constants.dataObject.getString("datatime"));

                        //存储工序ID 以及对应的JSONObject
                        Constants.alldata.put(proId, Constants.dataObject);

                        if( i==Constants.proChoose.size()) {
                            pager.removeAllViews();
                            // 如果本地存储的时间不为空 显示
                            if(Constants.timemap.get(0)!=null){
                                proTime.setText(Constants.timemap.get(0));
                                proTime.setTextColor(Color.BLACK);
                                proTime.setTextSize(24);
                            }
                            myPagerAdapter = new MyPagerAdapter(count, Constants.proChoose, getSupportFragmentManager());
                            pager.setAdapter(myPagerAdapter);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFail() {
                    System.out.println("on fail");
                }
                @Override
                public void onError() {
                    System.out.println("on error");
                }
            });
            baseNetTopBusiness.startRequest(dataRequest);
        }

        // 若用户选择的工序 >=1  显示第一个工序的名称
        if (Constants.proChoose != null && Constants.proChoose.size() >= 1) {
            proName.setText(Constants.proChoose.get(0));
            proName.setTextSize(20);
//          proName.setTextColor(Color.RED);
        }

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 清空本地缓存
                Constants.timemap.clear();
                Constants.alldata.clear();
                for (i = 0; i < Constants.proChoose.size(); i++) {
                    DataRequest dataRequest = new DataRequest();
                    proId = mGetProId.getId(Constants.proChoose.get(i));
                    //System.out.println("======proId=" + proId);
                    dataRequest.dataReq = mProChooseed.getParaIdJson(proId).toString();
                    BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                        @Override
                        public void onSuccess(HttpResponse response) {
                            //System.out.println("成功");
                            byte[] bytes = response.bytes;
                            try {
                                String str = new String(bytes, "gbk");
                                Constants.dataObject = new JSONObject(str);
                                Constants.timemap.put(proId, Constants.dataObject.getString("datatime"));
                                Constants.alldata.put(proId, Constants.dataObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(i==Constants.proChoose.size()) {
                                for(int j=0;j<Constants.proChoose.size();j++){
                                    ListViewFragment mListViewFragment =(ListViewFragment) myPagerAdapter.getFragment(j);
                                    // System.out.println(f.myAdapter);
                                    int proId = mListViewFragment.ProId;
                                    //System.out.println("shuaxin de proid:");
                                    // System.out.println(proId);
                                    mListViewFragment.updateData(proId);
                                    Log.v(LOG_TAG,Integer.toString(i));
                                    Log.v(LOG_TAG,Integer.toString(Constants.proChoose.size()));
                                    Toast.makeText(ShowDataActivity.this, "update!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFail() {
                            System.out.println("on fail");
                        }

                        @Override
                        public void onError() {
                            System.out.println("on error");
                        }

                    });
                    baseNetTopBusiness.startRequest(dataRequest);
                }
            }
        };
        mTimer.schedule(mTimerTask,5000,Integer.parseInt(Constants.frequency)*1000);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                // GetProId gpi = new GetProId();
                String str = Constants.proChoose.get(position);
                proName.setText(str);
                proName.setTextSize(16);
//                proName.setTextColor(Color.RED);
                proTime.setText(Constants.timemap.get(position));
                proTime.setTextColor(Color.GREEN);
                proTime.setTextSize(16);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
    }
}
