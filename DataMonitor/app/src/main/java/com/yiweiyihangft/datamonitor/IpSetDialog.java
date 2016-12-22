package com.yiweiyihangft.datamonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by 32618 on 2016/12/20.
 */

public class IpSetDialog {
    public Context context;
    public EditText ipset;
    public EditText portset;
    private SharedPreferences mSharedPreferences;
    private String my_ip;
    private String my_port;
    public IpSetDialog(Context context){
        this.context = context;
    }

    public void getDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.setting_button);
        builder.setTitle("请设置IP和端口");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(context).inflate(R.layout.ipsetdialog, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        ipset = (EditText) view.findViewById(R.id.ipset);
        portset = (EditText) view.findViewById(R.id.portset);
        mSharedPreferences = context.getSharedPreferences("MyProject", 0);
        String ip = mSharedPreferences.getString("ip", "");
        String port = mSharedPreferences.getString("port", "");

        if(!ip.equals("")){
            ipset.setText(ip);
        }
        if(!port.equals("")){
            portset.setText(port);
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                my_ip = ipset.getText().toString();
                my_port = portset.getText().toString();
                Constants.Url = my_ip+":"+my_port;
                System.out.println(Constants.Url);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("ip",my_ip);
                editor.putString("port",my_port);
                editor.commit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        builder.show();
    }

}
