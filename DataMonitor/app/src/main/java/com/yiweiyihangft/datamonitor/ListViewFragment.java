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
import android.widget.ListView;

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


public class ListViewFragment extends Fragment {
    //private List<String[]> table;
    public MyAdapter myAdapter=new MyAdapter();
    private ListView mylist;
    //static ListViewFragment listViewFragment;
    public int ProId;
    private String paradesc;
    private int paraId;
    private String[] paras;
    private GetParaId getParaId = new GetParaId();
    private ProChooseed pc = new ProChooseed();
    List<String[]> table1 =new ArrayList<String[]>();
    //myAdapter=new MyAdapter();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mylistview,container,false);

        //setContentView(R.layout.mylistview);
        // List<String[]> table=new ArrayList<String[]>();
        table1.clear();
        String[] str;
        str =new String[]{"测点描述","测点值","单位"} ;
        table1.add(str);
        //myAdapter.setData(table);
        Bundle b =getArguments();
        paras  = b.getStringArray("paralist");
        ProId= b.getInt("proId");

        //System.out.println("========"+ProId);
        //JSONObject object = Constants.alldata.get(proId);
        //System.out.println(object);
        GetSubString mGetSubString = new GetSubString();
        if(paras!=null) {
            for (int i=0;i<paras.length;i++) {

                try {
                    table1.add(new String[]{mGetSubString.getParadesc(paras[i]),
                            Constants.alldata.get(ProId).getString(Integer.toString(getParaId.getId(ProId,paras[i]))),
                            mGetSubString.getParaunit(paras[i])});
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        mylist = (ListView) rootView.findViewById(R.id.mylist);
        myAdapter.setData(table1, paras, ProId);
        //  myAdapter.notifyDataSetChanged();
        mylist.setAdapter(myAdapter);

        // 对List的每一个项目监听  用户点击后显示测点历史信息
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

     */
    public void updateData(int proId){
        table1.clear();
        String[] str;
        str =new String[]{"测点描述","测点值","单位"} ;
        table1.add(str);
//        pc = new ProChooseed();
//         String[] paras = pc.getPara(proId);
//        GetParaId getParaId = new GetParaId();
//        JSONObject object = Constants.alldata.get(proId);
//        System.out.println(object);
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