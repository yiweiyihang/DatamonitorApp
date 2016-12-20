package com.yiweiyihangft.datamonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_username;
    private EditText register_passwd;
    private EditText registerConf_passwd;
    private Button register_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_username=(EditText)findViewById(R.id.usernameReg_edit);
        register_passwd=(EditText)findViewById(R.id.passwordReg_edit);
        registerConf_passwd=(EditText)findViewById(R.id.pswdReg_Confirm_edit);
        register_submit=(Button)findViewById(R.id.register_bt);

        /*
          监听用户名的输入
         */
        register_username.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    if(register_username.getText().toString().trim().length()<4){
                        Toast.makeText(RegisterActivity.this, "用户名不能小于4个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        /*
          监听密码的输入
         */
        register_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (register_passwd.getText().toString().trim().length() < 6) {
                        Toast.makeText(RegisterActivity.this, "密码不能小于6个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        registerConf_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (!registerConf_passwd.getText().toString().trim().equals(register_passwd.getText().toString().trim())) {
                        Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        /*
          用户点击注册后执行的操作
         */
        register_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 检验输入信息的合法性 用户名不能为空 密码不能为空 两次密码输入不一致
                if (!checkEdit()) {
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this,"注册",Toast.LENGTH_SHORT).show();
//                    firstRequest arequest = new firstRequest();
//                    System.out.println(arequest.requestUrl);
//
//                    arequest.uname = register_username.getText().toString().trim();
//                    arequest.upwd = register_passwd.getText().toString().trim();
//                    arequest.flag = "reg";
//
//                    BaseNetTopBusiness baseNetTopBusiness=new BaseNetTopBusiness(new NetTopListener(){
//                        @Override
//                        public void onSuccess(HttpResponse response) {
//                            //System.out.println("成功");
//                            String s = new String(response.bytes);
//                            // System.out.println(s);
//                            if (s.equals("success") ) {
//                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                        @Override
//                        public void onFail() {
//                            System.out.println("on fail");
//                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//
//                        }
//                        @Override
//                        public void onError() {
//                            System.out.println("on error");
//                        }
//                    });
//                    baseNetTopBusiness.startRequest(arequest);
                }
            }

        });
    }


    /*
      检测用户注册信息的合法性
     */
    private boolean checkEdit(){
        if(register_username.getText().toString().trim().equals("")){
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(register_passwd.getText().toString().trim().equals("")){
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!register_passwd.getText().toString().trim().equals(registerConf_passwd.getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }
}
