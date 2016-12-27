package com.yiweiyihangft.datamonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import netRequest.BaseNetTopBusiness;
import netRequest.ForthRequest;
import netRequest.HttpResponse;
import netRequest.NetTopListener;

public class UserSetActivity extends AppCompatActivity {
    private EditText original_userpwd;
    private EditText new_passwd;
    private EditText confirm_passwd;
    private Button change_submit;
    private Button frequencySet_bt;
    private EditText frequency_edit;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set);
        initView();
    }

    private void initView() {
        frequency_edit = (EditText) findViewById(R.id.frequency_Edit);
        frequencySet_bt = (Button) findViewById(R.id.fre_conf_bt);
        original_userpwd = (EditText) findViewById(R.id.original_userpwd_edit);
        new_passwd = (EditText) findViewById(R.id.new_passwd_edit);
        confirm_passwd = (EditText) findViewById(R.id.confirm_passwd_edit);
        change_submit = (Button) findViewById(R.id.change_submit);
        sp = getSharedPreferences("Myproject",0);
        String frequency = sp.getString("frequency","");
        if (!frequency.equals("")) {
            frequency_edit.setText(frequency);
            Constants.frequency = frequency;
        }

        // 监听频率设置确认按钮
        frequencySet_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String frequency = frequency_edit.getText().toString();
                SharedPreferences.Editor editor = sp.edit();  // 编辑用户偏好设置
                // 如果登陆成功 用户选择记住密码  则将输入的用户信息存入SharedPreference中
                if(!frequency.equals("")){
                    editor.putString("frequency",frequency);
                }
                editor.commit();     // 提交用户偏好设置信息
                Constants.frequency = frequency_edit.getText().toString();  // 获取用户频率要求
                Toast.makeText(UserSetActivity.this, "频率设置成功:"+Constants.frequency.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // 监听初始密码输入框
        original_userpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (original_userpwd.getText().toString().trim().length() < 6) {
                        Toast.makeText(UserSetActivity.this, "密码不能小于6个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 监听新密码输入框
        new_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (new_passwd.getText().toString().trim().length() < 6) {
                        Toast.makeText(UserSetActivity.this, "密码不能小于6个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 监听确认密码输入框
        confirm_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (!confirm_passwd.getText().toString().trim().equals(new_passwd.getText().toString().trim())) {
                        Toast.makeText(UserSetActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        // 监听修改密码确认按钮
        change_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkedit()) {
                    return;
                } else {
                    ForthRequest arequest = new ForthRequest();
                    arequest.orgpwd = original_userpwd.getText().toString();
                    arequest.newpwd = new_passwd.getText().toString();
                    arequest.username = Constants.UserName;
                    System.out.println(arequest.requestUrl);
                    System.out.println(arequest.orgpwd);
                    System.out.println(arequest.newpwd);
                    System.out.println(arequest.username);
                    BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
                        @Override
                        public void onSuccess(HttpResponse response) {
                            System.out.println("成功");
                            byte[] bytes = response.bytes;
                            try {
                                String str = new String(bytes, "gbk");
                                //System.out.println(new String(bytes, "gbk"));
                                if (str.equals("success")) {
                                    Toast.makeText(UserSetActivity.this, "修改密码成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserSetActivity.this, "您的密码输入不正确！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail() {
                            System.out.println("on fail");
                            Toast.makeText(UserSetActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            System.out.println("on error");
                        }
                    });
                    baseNetTopBusiness.startRequest(arequest);  //发送修改密码申请
                }

            }
        });
    }

    private boolean checkedit() {
        if (!new_passwd.getText().toString().trim().equals(confirm_passwd.getText().toString().trim())) {
            Toast.makeText(UserSetActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
