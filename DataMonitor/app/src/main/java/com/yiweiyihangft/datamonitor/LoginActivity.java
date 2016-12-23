package com.yiweiyihangft.datamonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import netRequest.BaseNetTopBusiness;
import netRequest.HttpResponse;
import netRequest.NetTopListener;
import netRequest.firstRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "LoginActivity";
    /*
     * 定义各种变量
     */
    private EditText login_username;
    private EditText login_password;
    private EditText login_url;
    private Button ipsetbtn;
    private Button user_login_button;
    private Button user_register_button;
    private Button bt_pwd_eye;
    private Button bt_username_clear;
    private CheckBox userPswd_CheckB;
    private SharedPreferences sp;
    private String my_ip;
    private String my_name;
    private String my_pswd;
    private boolean flag;     // 判断是否保存用户名和密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);
        initView();
    }

    /*
     * 初始化登录页面
     */
    private void initView() {
        login_username = (EditText) findViewById(R.id.username_edit);
        login_password = (EditText) findViewById(R.id.password_edit);
        userPswd_CheckB = (CheckBox) findViewById(R.id.save_psw_checkB);
        ipsetbtn = (Button) findViewById(R.id.iP_set_bt);
        user_login_button = (Button) findViewById(R.id.login_bt);
        user_register_button = (Button) findViewById(R.id.register_bt);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);

        sp = getSharedPreferences("MyProject", 0);   // 读取用户偏好设置
        String ip = sp.getString("ip", "");
        String port = sp.getString("port", "");
        Constants.Url = ip + ":" + port;     // Build URL
        String name = sp.getString("name", "");     // (String key, String default_value)
        String pswd = sp.getString("pswd", "");
        flag = sp.getBoolean("flag", false);
        userPswd_CheckB.setChecked(flag);

        // 判断是否有之前存储的用户账号保存 如有 显示在页面中
        if (!name.equals("") && flag == true) {
            login_username.setText(name);
        }
        if (!pswd.equals("") && flag == true) {
            login_password.setText(pswd);
        }

        // 设置对登录、注册、ip设置、密码可见、用户名清除button的OnClicklistener
        user_login_button.setOnClickListener(this);
        user_register_button.setOnClickListener(this);
        ipsetbtn.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        bt_username_clear.setOnClickListener(this);


        // 监听EditText的用户名是否被更改
        login_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 若用户名发生更改 获取用户名
                if (!hasFocus) {
                    String username = login_username.getText().toString().trim();
                    // 判断用户名长度 违反要求则弹出Toast提示
                    if (username.length() < 3) {
                        Toast.makeText(LoginActivity.this, "用户名不能小于4个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        // 监听EditText密码是否被更改
        login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果密码被更改 读取密码
                if (!hasFocus) {
                    String password = login_password.getText().toString().trim();
                    // 判断密码长度是否合法 违反则弹出Toast提示
                    if (password.length() < 4) {
                        Toast.makeText(LoginActivity.this, "密码不能小于4个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        // 监听 保存密码复选框 是否被选中
        userPswd_CheckB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flag= true;   // 选中则flag为true保存密码

                } else {
                    flag = false;
                }
            }
        });
    }


    /*
     * 判断哪个button被点击
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            /*
             用户点击登录按钮执行的操作
             */
            case R.id.login_bt:
                if (checkEdit()) {         // 检查用户密码EditText是否有效 返回布尔值
                    login();               //处理用户登录操作
                }
                break;
            /*
             用户点击注册按钮执行的操作
             */
            case R.id.register_bt:
                // 发送显示Intent 进入用户注册Activity
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            /*
             用户点击IP设置 创建IpSetDialog对象
             */
            case R.id.iP_set_bt:   //
                IpSetDialog ipSetDialog = new IpSetDialog(this);
                ipSetDialog.getDialog();
                Log.v(LOG_TAG,"IP设置");
                break;
            /*
             清除用户名及密码
             */
            case R.id.bt_username_clear:
                login_username.setText("");
                login_password.setText("");
                Log.v(LOG_TAG,"清除用户信息");
                break;
            /*
             设置密码的可见性
             */
            case R.id.bt_pwd_eye:
                if (login_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    bt_pwd_eye.setBackgroundResource(R.drawable.bt_eye_on);
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    bt_pwd_eye.setBackgroundResource(R.drawable.bt_eye_on);
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                login_password.setSelection(login_password.getText().toString().length());
                break;
        }
    }

    /*
    检查用户名和密码是否为空  返回一个布尔值
    */
    private boolean checkEdit() {
        if (login_username.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (login_password.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    /*
    处理用户登录操作
     */
    private void login() {

        firstRequest arequest = new firstRequest();

        // 读入登录页面的用户信息和密码
        // 填充Request所需的信息
        arequest.uname = login_username.getText().toString().trim();   // trim():去掉字符串首尾的空格
        arequest.upwd = login_password.getText().toString().trim();
        my_name = login_username.getText().toString();
        my_pswd = login_password.getText().toString();
        arequest.flag = "log";    // flag标志用来表示对数据库user表的登录操作

        /*
        接口: NetTopListener()  method: onSuccess() onFail()  onError() 需要进行方法重写
        BaseNetTopBusiness 负责与Web服务器连接 发送请求 待看！！
         */
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {

            /*
             * 连接成功的处理
             */
            @Override
            public void onSuccess(HttpResponse response) {
                System.out.println("成功");
                String s = new String(response.bytes);
                System.out.println(s);

                // 判断用户登录是否成功
                if (s.equals("success") ) {
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sp.edit();  // 编辑用户偏好设置
                    Constants.UserName = my_name;    //保存用户名

                    // 如果登陆成功 用户选择记住密码  则将输入的用户信息存入SharedPreference中
                    if(flag){
                        editor.putString("name",my_name);
                        editor.putString("pswd",my_pswd);
                    }
                    editor.putBoolean("flag", flag);   // 将用户密码已保存标志存入SharedPreference
                    editor.commit();     // 提交用户偏好设置信息

                    // 发送显示Intent  切换到ActionBarActivity
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败，用户名或密码输入不正确！", Toast.LENGTH_SHORT).show();
                }
            }

            /*
             * 连接失败的处理
             */
            @Override
            public void onFail() {
                System.out.println("on fail");
                Toast.makeText(LoginActivity.this, "登录失败，用户名或密码或URL输入不正确！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                System.out.println("on error");
            }
        });
        // 发送Request连接请求
        baseNetTopBusiness.startRequest(arequest);
    }


//    private void login() {
//        Toast.makeText(LoginActivity.this, "登录！", Toast.LENGTH_SHORT).show();
//        Toast.makeText(LoginActivity.this, login_username.getText().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(LoginActivity.this, login_password.getText().toString(), Toast.LENGTH_SHORT).show();
//        Intent welcomeI = new Intent(LoginActivity.this,WelcomeActivity.class);
//        startActivity(welcomeI);
//    }
}
