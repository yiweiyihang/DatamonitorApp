package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.yiweiyihangft.datamonitor.utils.DataModel;
import com.yiweiyihangft.datamonitor.utils.LineChartModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import netRequest.BaseNetTopBusiness;
import netRequest.FiveRequest;
import netRequest.HttpResponse;
import netRequest.NetTopListener;

public class ShowLineChart extends AppCompatActivity {
    private LineChart mLineChart;
    DataModel dataModel=null;
    String timeSpace;
    Spinner datespinner;
    private TextView Paradesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_line_chart);
        Toast.makeText(ShowLineChart.this,"show linechart",Toast.LENGTH_SHORT).show();
        datespinner = (Spinner) findViewById(R.id.spinner2);
        Paradesc = (TextView)findViewById(R.id.Paradesc);

        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        final int proId = b.getInt("proID");
        final int paraId = b.getInt("paraID");
        Paradesc.setText(b.getString("paradesc"));
        datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refresh(proId,paraId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mLineChart = (LineChart) findViewById(R.id.linechart);
        dataModel = DataModel.getInstance();
        LineChartModel model = dataModel.getLineChartModel(10);
        LineData lineData = getLineData(model);
        showChart(mLineChart, lineData, Color.rgb(114, 188, 223));
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        //  lineChart.setDescription("发顺丰");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }


    /**
     * 生成一个数据
     //  * @param count 表示图表中有多少个坐标点
     //  * @param range 用来生成range以内的随机数
     * @return
     */
    private LineData getLineData(LineChartModel model) {
        //   ArrayList<String> xValues = new ArrayList<String>();
        //   for (int i = 0; i < count; i++) {
        // x轴显示的数据，这里默认使用数字下标显示
        //      xValues.add("" + i*2);
        //   }
        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(model.getyValues(), "测试折线图" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        List<ILineDataSet> l=new ArrayList<ILineDataSet>();
        l.add(lineDataSet);
        // create a data object with the datasets
        LineData lineData = new LineData(model.getxValues(),l);
        return lineData;
    }

    public void refresh(int proId,int paraId)
    {
        timeSpace = datespinner.getSelectedItem().toString().split(" ")[0];
//        System.out.println("fffffffffffffff");
//        System.out.println(timeSpace);
//        System.out.println (proId);
//        System.out.println(paraId);

        FiveRequest aRequest = new FiveRequest();
        aRequest.proID = Integer.toString(proId);
        aRequest.paraID = Integer.toString(paraId);
        aRequest.timeSpace = timeSpace;
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                byte[] bytes = response.bytes;
                try {
                    String str = new String(bytes, "gbk");
                    JSONObject object = new JSONObject(str);
                    Constants.historydata = object;
                    System.out.println("********************--------");
                    System.out.println(object);
                    LineChartModel model=dataModel.getLineChartModel(Integer.parseInt(timeSpace));
                    LineData lineData=getLineData(model);
                    showChart(mLineChart, lineData, Color.rgb(114, 188, 223));
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
        baseNetTopBusiness.startRequest(aRequest);
    }


}
