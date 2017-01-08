package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mobeta.android.dslv.DragSortListView;
import com.yiweiyihangft.datamonitor.Adapter.MyAdapter;
import com.yiweiyihangft.datamonitor.utils.GetParaId;
import com.yiweiyihangft.datamonitor.utils.GetSubString;
import com.yiweiyihangft.datamonitor.utils.ProChooseed;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import netRequest.BaseNetTopBusiness;
import netRequest.FiveRequest;
import netRequest.HttpResponse;
import netRequest.NetTopListener;

/**
 * ShowData 显示多个测点的Page Fragment
 */

public class ListViewFragment extends Fragment {
    //private List<String[]> table;
    public MyAdapter myAdapter=new MyAdapter();
    private DragSortListView mylist;
    //static ListViewFragment listViewFragment;
    public int ProId;
    private String paradesc;
    private int paraId;
    private String[] paras;  // 测点列表 未切分
    private GetParaId getParaId = new GetParaId();
    private ProChooseed pc = new ProChooseed();
    List<String[]> table1 =new ArrayList<String[]>();
    private GetSubString mGegSubString = new GetSubString();

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    String item=myAdapter.getItem(from);
                    try{
                        String[] item_apart = new String[]
                                {mGegSubString.getParadesc(item),
                                        Constants.alldata.get(ProId).getString(Integer.toString(getParaId.getId(ProId,item))),
                                        mGegSubString.getParaunit(item)};
                        myAdapter.notifyDataSetChanged();
                        myAdapter.remove(from);
                        myAdapter.insert(item_apart, to);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    myAdapter.remove(which);
                }
            };

    private DragSortListView.DragScrollProfile ssProfile =
            new DragSortListView.DragScrollProfile() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mylistview,container,false);

        //setContentView(R.layout.mylistview);
        // List<String[]> table=new ArrayList<String[]>();

        mylist = (DragSortListView)rootView.findViewById(R.id.mylist);
        mylist.setDropListener(onDrop);
        mylist.setRemoveListener(onRemove);
        mylist.setDragScrollProfile(ssProfile);

        table1.clear();
        String[] str;
        str =new String[]{"测点描述","测点值","单位"} ;
        table1.add(str);
        //myAdapter.setData(table);

        // 从PageAdapter 获取测点列表和工序ID信息
        Bundle b =getArguments();
        paras  = b.getStringArray("paralist");
        ProId= b.getInt("proId");

        //System.out.println("========"+ProId);
        //JSONObject object = Constants.alldata.get(proId);
        //System.out.println(object);

        // 将测点描述进行切分

        if(paras!=null) {
            for (int i=0;i<paras.length;i++) {

                try {
                    table1.add(new String[]{mGegSubString.getParadesc(paras[i]),
                            Constants.alldata.get(ProId).getString(Integer.toString(getParaId.getId(ProId,paras[i]))),
                            mGegSubString.getParaunit(paras[i])});
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
//        mylist = (ListView) rootView.findViewById(R.id.mylist);
        myAdapter.setData(table1, paras, ProId);
        //  myAdapter.notifyDataSetChanged();
        mylist.setAdapter(myAdapter);


        /*
         * 设置用户点击测点后的操作  显示线性图
         */
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
                System.out.println("**********");
                System.out.println(position);
                System.out.println(id);
                paradesc = (String) myAdapter.getItem(position);
                paraId = position;
                System.out.println(paradesc);
                System.out.println("**********");
                //proId = position;
                FiveRequest aRequest = new FiveRequest();
                aRequest.proID = Integer.toString(ProId);
                aRequest.paraID = Integer.toString(position);
                aRequest.timeSpace = "10";
                BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        // System.out.println("成功");
                        byte[] bytes = response.bytes;
                        try {
                            String str = new String(bytes, "gbk");
                            JSONObject object = new JSONObject(str);
                            Constants.historydata = object;
                            // System.out.println("********************--------");
                            // System.out.println(object);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getActivity(),ShowLineChart.class);
                        Bundle b = new Bundle();
                        b.putString("paradesc", paradesc);
                        b.putInt("proID", ProId);
                        b.putInt("paraID", paraId);
                        i.putExtras(b);
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
                baseNetTopBusiness.startRequest(aRequest);


            }
        });
        return rootView;
    }


    /*
     * 更新数据
     */
    public void updateData(int proId){
        table1.clear();
        String[] str;
        str =new String[]{"测点描述","测点值","单位"} ;
        table1.add(str);
        //pc = new ProChooseed();
        // String[] paras = pc.getPara(proId);
        //GetParaId getParaId = new GetParaId();
        //JSONObject object = Constants.alldata.get(proId);
        //System.out.println(object);
        if(paras!=null) {
            GetSubString gs = new GetSubString();
            for (int i=0;i<paras.length;i++) {
                try {
                    table1.add(new String[]{gs.getParadesc(paras[i]),
                            Constants.alldata.get(proId).getString(Integer.toString(getParaId.getId(proId,paras[i]))),
                            gs.getParaunit(paras[i])});

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("dsaaaaadasdsasdas==============");
        System.out.println(proId);
        System.out.println(paras);
        if(table1==null)
        {
            System.out.println("null");
        }
        if(myAdapter==null)
        {
            System.out.println("adapter null");
        }
        myAdapter.setData(table1, paras, proId);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        super.onAttach(context);
    }
//    public static ListViewFragment newInstance(){
//        if(listViewFragment==null) {
//            //listViewFragment = new ListViewFragment(String );
//
//            return listViewFragment;
//        }
//        return listViewFragment;
//    }

}
