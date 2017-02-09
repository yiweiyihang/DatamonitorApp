package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import static com.yiweiyihangft.datamonitor.Constants.context;

public class ShowDataActivity extends AppCompatActivity {
    private TextView proName;
    private TextView proTime;
    private static MyPagerAdapter myPagerAdapter;
    private ViewPager pager;
    private int count;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int i;
    private  GetProId getProId = new GetProId();
    private  ProChooseed pc = new ProChooseed();
    private int proId;

    /**
     * 用户频率设置
     */
    private SharedPreferences sp ;
    private String frequency = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        context=this;
        // System.out.println("************************---------");
        pager = (ViewPager) findViewById(R.id.pagers);
        Intent intent = this.getIntent();
        Bundle bundle=intent.getExtras();
        count=bundle.getInt("count");
        //System.out.println("count=" + count);
        proName = (TextView) findViewById(R.id.proName);
        proTime = (TextView) findViewById(R.id.proTime);
        Constants.alldata.clear();
        Constants.timemap.clear();
        sp = getSharedPreferences("Myproject",0);
        frequency = sp.getString("frequency","");
        if (!frequency.equals("")) {
            Constants.frequency = frequency;
        }

//        final ProChooseed pc = new ProChooseed();
        for(i=0;i<Constants.proChoose.size();i++) {
            DataRequest dataRequest = new DataRequest();
            final int proId = getProId.getId(Constants.proChoose.get(i));
            System.out.println("======proId="+proId);
            dataRequest.dataReq = pc.getParaIdJson(proId).toString();
            BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    System.out.println("成功");
                    byte[] bytes = response.bytes;
                    try {
                        String str = new String(bytes, "gbk");
                        Constants.dataObject = new JSONObject(str);
                        Constants.timemap.put(proId,Constants.dataObject.getString("datatime"));
                        //System.out.println(Constants.timemap);
                        Constants.alldata.put(proId, Constants.dataObject);
//                        System.out.println("====");
//                        System.out.println(Constants.dataObject);
//                        System.out.println("====");
//                        System.out.println(new String(bytes, "gbk"));
                        if(i==Constants.proChoose.size()) {
                            pager.removeAllViews();
                            if(Constants.timemap.get(0)!=null){
                                proTime.setText(Constants.timemap.get(0));
                                proTime.setTextColor(Color.GREEN);
                                proTime.setTextSize(16);
                            }
                            // 绑定页面适配器
                            myPagerAdapter = new MyPagerAdapter(count, Constants.proChoose, getSupportFragmentManager());
//                            System.out.println("---------");
//                            System.out.println(Constants.alldata);

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
        if (Constants.proChoose != null && Constants.proChoose.size() >= 1) {
            // 默认显示第一个工序名称
            proName.setText(Constants.proChoose.get(0));
            // 设置工序显示字体
            proName.setTextSize(16);
            // 设置工序显示颜色
            proName.setTextColor(Color.RED);
        }

        /**
         * 定时器根据用户频率设定发送请求
         */
        mTimer = new Timer();
        // 设定定时器任务
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
//                Constants.timemap.clear();
                Constants.alldata.clear();
                for (i = 0; i < Constants.proChoose.size(); i++) {
                    DataRequest dataRequest = new DataRequest();
                    proId = getProId.getId(Constants.proChoose.get(i));
                    //System.out.println("======proId=" + proId);
                    dataRequest.dataReq = pc.getParaIdJson(proId).toString();
                    BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                        @Override
                        public void onSuccess(HttpResponse response) {
                            System.out.println("成功");
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
                                    ListViewFragment f =(ListViewFragment) myPagerAdapter.getFragment(j);
                                    // System.out.println(f.myAdapter);
                                    int proId = f.ProId;
                                    //System.out.println("shuaxin de proid:");
                                    // System.out.println(proId);
                                    f.updateData(proId);
                                    Toast.makeText(ShowDataActivity.this,"update!!",Toast.LENGTH_SHORT).show();
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

        // 读取用户频率设定 定时器任务绑定
        int frequencySet = Integer.parseInt(Constants.frequency) * 1000;
        mTimer.schedule(mTimerTask,frequencySet,frequencySet);
        /**
         * 设定用户滑动页面的操作
         */
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 设置当前页面的显示格式
             * @param position 当前页面索引值
             */
            @Override
            public void onPageSelected(final int position) {
                // GetProId gpi = new GetProId();
                String str = Constants.proChoose.get(position);
                proName.setText(str);
                proName.setTextSize(16);
                proName.setTextColor(Color.RED);
                proTime.setText(Constants.timemap.get(position));
                proTime.setTextColor(Color.GREEN);
                proTime.setTextSize(16);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     *  设定当前活动停止时的操作
     */
    @Override
    protected void onStop() {
        super.onPause();
        mTimer.cancel();
        Constants.paramap.clear();
    }
}


