package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.yiweiyihangft.datamonitor.Adapter.MyAdapter;
import com.yiweiyihangft.datamonitor.utils.GetParaId;
import com.yiweiyihangft.datamonitor.utils.GetSubString;

import org.json.JSONObject;

import java.util.ArrayList;

import netRequest.BaseNetTopBusiness;
import netRequest.FiveRequest;
import netRequest.HttpResponse;
import netRequest.NetTopListener;

/**
 * ShowData 显示测点表格的Page Fragment
 */

public class ListViewFragment extends Fragment {
    /**
     * LOG标记
     */
    final String LOG_TAG = "ListViewFragment";
    /**
     * 测点显示单行格式适配器
     */
    public MyAdapter myAdapter = new MyAdapter();
    /**
     * 支持拖拽排序的ListView
     */
    private DragSortListView mylist;
    /**
     * 工序对应ID
     */
    public int ProId;
    /**
     * 测点描述列表(未切分)
     */
    private String[] paras;
    /**
     * 测点描述  eg:3.2MPa蒸汽流量(t/h)
     */
    private String paradesc;
    /**
     * 测点对应ID
     */
    private long paraId;

    /**
     * 测点信息表格(切分) 每次update都恢复为默认排序
     */
    public ArrayList<String[]> table = new ArrayList<String[]>();

    /**
     * 测点信息表格(切分) 根据用户需求重排序
     */
    private ArrayList<String[]> table_resort = new ArrayList<String[]>();

    /**
     * 用户设置的显示顺序(拖拽)
     */
    private ArrayList<Integer> mapSort;

    /**
     * 拖拽释放操作监听器
     */
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    // 获得拖拽源的单个切分测点信息
                    String[] item_apart = table_resort.get(from);
                    // 从选中位置删除当前项
                    table_resort.remove(from);
                    // 向目标位置插入项
                    table_resort.add(to, item_apart);
                    // 更新用户排序设置
                    int order = removeMap(from);
                    insertMap(order, to);
                    //监听适配器数据源变化
                    myAdapter.notifyDataSetChanged();

                    /***************调试专用*************/
                    Log.v(LOG_TAG, "from=========" + order);
                    Log.v(LOG_TAG, "to============" + to);
                    Log.v(LOG_TAG, "item_select" + item_apart);

                    for (int i = 0; i < mapSort.size(); i++) {
                        Log.v(LOG_TAG, "map" + "i=  " + i + "sort====" + mapSort.get(i));

                    }
                    for (int j = 0; j < table_resort.size(); j++) {
                        System.out.println(/***************调试专用*****************/);
                        System.out.println("table" + j + "    " + table_resort.get(j));
                        System.out.println("table_resort" + j + "    " + table_resort.get(j));
                        System.out.println(/***************调试专用*****************/);

                    }
                    /***************调试专用*************/
                }

            };

    /**
     * 左滑删除操作监听器(已禁止)
     */
    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    Toast.makeText(getContext(), "remove", Toast.LENGTH_SHORT).show();

                }
            };
    /**
     * 快速滑动条
     */
    private DragSortListView.DragScrollProfile ssProfile =
            new DragSortListView.DragScrollProfile() {
                // 初始化快速滑动条
                @Override
                public float getSpeed(float w, long t) {
                    if (w > 0.8f) {
                        // Traverse all views in a millisecond
                        return ((float) myAdapter.getCount()) / 0.001f;
                    } else {
                        return 10.0f * w;
                    }
                }
            };
    /**
     * 创建页面
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        // 初始化生成页面
        View rootView = inflater.inflate(R.layout.mylistview, container, false);

        // 初始化监听器设置list的拖住排序操作
        mylist = (DragSortListView) rootView.findViewById(R.id.mylist);
        mylist.setDropListener(onDrop);
        mylist.setRemoveListener(onRemove);
        mylist.setDragScrollProfile(ssProfile);

        // 清除测点信息缓存
        table.clear();
        table_resort.clear();

        // 从PageAdapter 获取测点列表和工序ID信息
        Bundle b = getArguments();
        paras = b.getStringArray("paralist");
        ProId = b.getInt("proId");

        // 初始化table 获取测点信息表格
        paraSub(paras);
        // 初始化用户排序Map
        initMap(table.size());
        // 初始化table_resort(根据用户排序)
        for (int j = 0; j < table.size(); j++) {
            table_resort.add(j, table.get(getIndex(j)));
        }

        /***************调试专用*****************/
        for (int j = 0; j < table_resort.size(); j++) {
            System.out.println(/***************调试专用*****************/);
            System.out.println("table" + j + "    " + table_resort.get(j));
            System.out.println("table_resort" + j + "    " + table_resort.get(j));
            System.out.println(/***************调试专用*****************/);

        }
        /***************调试专用*****************/
        // 初始化Adapter要显示的信息
        myAdapter.setData(table_resort, paras, ProId);
        // 初始化绑定的单项适配器
        mylist.setAdapter(myAdapter);
        // 设置默认可排序
        mylist.setDragEnabled(true);

        // 设置用户点击测点后的操作  显示线性图  转到ShowLineChart
        OnItemClickListener(mylist);
        return rootView;
    }

    /**
     * 更新数据
     * @param proId 工序对应ID
     */
    public void updateData(int proId) {
        // 清除缓存
        table.clear();
        table_resort.clear();
        //将测点描述信息进行切分 刷新table数据
        paraSub(paras);

        /***************** 调试专用 *******************/
        System.out.println("dsaaaaadasdsasdas==============");
        System.out.println(proId);
        System.out.println(paras);
        /***************** 调试专用 *******************/

        if (table == null) {
            System.out.println("null");
        }
        if (myAdapter == null) {
            System.out.println("adapter null");
        }
        // 根据用户排序刷新table_resort
        for (int j = 0; j < table.size(); j++) {
            table_resort.add(j, table.get(getIndex(j)));
        }
        // 测点单条显示适配器更新数据
        myAdapter.setData(table_resort, paras, proId);
        // 单条显示适配器适配器监听数据变化
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 获得table数据(初始化和刷新)
     * 切分测点描述信息  读取测点值添加到table中
     * @param paras 未切分的测点信息列表
     */
    private void paraSub(String[] paras) {
        if (paras != null) {
            for (int i = 0; i < paras.length; i++) {
                try {
                    // 获取对应ID的测点Json数据
                    JSONObject paraJson = Constants.alldata.get(ProId);
                    // 获取数据库中的测点ID
                    int paraId_DB = GetParaId.getId(ProId, paras[i]);
                    // 获得测点值
                    String paraJString = paraJson.getString(Integer.toString(paraId_DB));
                    // 添加测点名称，测点值，测点单位到table中
                    table.add(new String[]{
                            GetSubString.getParadesc(paras[i]),
                            paraJString,
                            GetSubString.getParaunit(paras[i])});

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        super.onAttach(context);
    }

    /**
     * 设置用户点击测点后的操作  显示线性图
     *
     * @param mylist 绑定的支持拖拽排序的ListView
     */
    private void OnItemClickListener(DragSortListView mylist) {
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * 监测测点单项的点击
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获得 测点信息描述  eg:3.2MPa蒸汽流量(t/h)
                paradesc = myAdapter.getItem(getIndex(position));
                // 获得测点对应ID
                paraId = myAdapter.getItemId(getIndex(position));
                // 发送测点时间序列显示请求
                FiveRequest aRequest = new FiveRequest();
                aRequest.proID = Integer.toString(ProId);
                aRequest.paraID = Long.toString(paraId);
                aRequest.timeSpace = "10";
                // 发送请求 根据返回的信息回调函数
                BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        // System.out.println("成功");
                        byte[] bytes = response.bytes;
                        try {
                            String str = new String(bytes, "gbk");
                            JSONObject object = new JSONObject(str);
                            Constants.historydata = object;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 新建 时间序列显示Intent
                        Intent i = new Intent(getActivity(), ShowLineChart.class);
                        // 打包测点描述 工序ID 测点ID 信息
                        Bundle b = new Bundle();
                        b.putString("paradesc", paradesc);
                        b.putInt("proID", ProId);
                        b.putLong("paraID", paraId);
                        i.putExtras(b);
                        // 转到 时间序列显示
                        startActivity(i);

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
                // 发送网络请求
                baseNetTopBusiness.startRequest(aRequest);
            }
        });
    }


    /**
     * 初始化用户排序
     *
     * @param size 测点table数量
     */
    private void initMap(int size) {
        mapSort = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            mapSort.add(i);
        }
    }

    /**
     * 从用户排序映射中删除选中项
     * @param from 用户移动起始点
     * @return 当前显示位置
     */
    public int removeMap(int from) {
        int temp = mapSort.get(from);
        mapSort.remove(from);
        return temp;

    }

    /**
     * 向用户排序映射插入用户拖动的目标位置
     * @param order 用户排序列表起始索引
     * @param to    用户目标索引
     */
    public void insertMap(int order, int to) {
        mapSort.add(to, order);
    }

    /**
     * 获得当前位置的测点索引值
     * @param position
     * @return
     */
    public int getIndex(int position) {
        return mapSort.get(position);
    }
}
