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

/**
 * 用户登录初始页面
 *
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 标记
     */
    final String LOG_TAG = "LoginActivity";
    /**
     * 用户登录名输入框
     */
    private EditText login_username;
    /**
     * 用户密码输入框
     */
    private EditText login_password;
//    private EditText login_url;
    /**
     * IP设置按钮
     */
    private Button ipsetbtn;
    /**
     * 登录按钮
     */
    private Button user_login_button;
    /**
     * 注册按钮
     */
    private Button user_register_button;
    /**
     * 用户密码可见按钮(眼睛)
     */
    private Button bt_pwd_eye;
    /**
     * 清除用户名按钮(叉子)
     */
    private Button bt_username_clear;
    /**
     * 记住密码选框
     */
    private CheckBox userPswd_CheckB;
    /**
     * SharedPreferences 存储
     */
    private SharedPreferences mSharedPreferences;
    private String my_ip;
    /**
     * 用户名
     */
    private String my_name;
    /**
     * 用户密码
     */
    private String my_pswd;
    /**
     * 保存用户名密码标志
     */
    private boolean isSavedFlag;     // 判断是否保存用户名和密码

    /**
     * 创建Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // 绑定页面
        setContentView(R.layout.activity_login);
        //初始化登录页面
        initView();
    }

    /**
     * 初始化登录页面
     */
    private void initView() {
        // 关联页面元素
        login_username = (EditText) findViewById(R.id.username_edit);
        login_password = (EditText) findViewById(R.id.password_edit);
        userPswd_CheckB = (CheckBox) findViewById(R.id.save_psw_checkB);
        ipsetbtn = (Button) findViewById(R.id.iP_set_bt);
        user_login_button = (Button) findViewById(R.id.login_bt);
        user_register_button = (Button) findViewById(R.id.register_bt);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);

        // 利用Context类中的 getSharedPreferences()方法获得SharedPreferences对象
        mSharedPreferences = getSharedPreferences("MyProject", 0);
        // 读取用户IP设置
        String ip = mSharedPreferences.getString("ip", "");
        // 读取用户端口设置
        String port = mSharedPreferences.getString("port", "");
        // 将Url(IP + 端口)保存到数据源中
        Constants.Url = ip + ":" + port;
        // 读取用户名  get方法两个参数：键 & 默认值
        String name = mSharedPreferences.getString("name", "");
        // 读取密码
        String pswd = mSharedPreferences.getString("pswd", "");
        // 读取记住密码选择
        isSavedFlag = mSharedPreferences.getBoolean("flag", false);
        // 选框显示记住密码选择状态
        userPswd_CheckB.setChecked(isSavedFlag);

        // 显示之前保存的用户名和密码
        if (!name.equals("") && isSavedFlag == true) {
            login_username.setText(name);
        }
        if (!pswd.equals("") && isSavedFlag == true) {
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
                // 若用户名发生更改 读取用户名
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
                    isSavedFlag = true;   // 选中则flag为true保存密码

                } else {
                    isSavedFlag = false;
                }
            }
        });
    }


    /**
     * 处理 button 点击
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // 用户点击登录按钮
            case R.id.login_bt:
                if (checkEdit()) {         // 检查用户密码EditText是否有效 返回布尔值
                    login();               //处理用户登录操作
                }
                break;
            // 用户点击注册按钮
            case R.id.register_bt:
                // 发送显示Intent 进入用户注册Activity
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            // 用户点击IP设置 创建IpSetDialog对象
            case R.id.iP_set_bt:
                IpSetDialog ipSetDialog = new IpSetDialog(this);
                // 显示IP设置对话框
                ipSetDialog.getDialog();
                Log.v(LOG_TAG,"IP设置");
                break;
            // 清除用户名及密码
            case R.id.bt_username_clear:
                login_username.setText("");
                login_password.setText("");
                Log.v(LOG_TAG,"清除用户信息");
                break;
            // 设置密码的可见性
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


    /**
     * 检查用户名和密码是否为空  返回一个布尔值
     * @return
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

    /**
     * 处理用户登录操作
     */
    private void login() {
        // 新建第一类请求对象
        firstRequest arequest = new firstRequest();

        // 读入登录页面的用户信息和密码 填充Request所需的信息
        // trim():去掉字符串首尾的空格
        arequest.uname = login_username.getText().toString().trim();
        arequest.upwd = login_password.getText().toString().trim();
        // 保存用户名和密码
        my_name = login_username.getText().toString();
        my_pswd = login_password.getText().toString();
        // 标志登录操作
        arequest.flag = "log";    // flag标志用来表示对数据库user表的登录操作

        /*
        接口: NetTopListener()  method: onSuccess() onFail()  onError() 需要进行方法重写
        BaseNetTopBusiness 负责与Web服务器连接 发送请求 待看！！
         */
        BaseNetTopBusiness baseNetTopBusiness = new BaseNetTopBusiness(new NetTopListener() {
            @Override
            // 连接成功
            public void onSuccess(HttpResponse response) {
                /*********调试专用**********/
                System.out.println("成功");
                String s = new String(response.bytes);
                System.out.println(s);
                /*********调试专用**********/

                // 判断用户登录是否成功
                if (s.equals("success") ) {
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    // 存储用户信息数据
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    //向数据源中保存用户名
                    Constants.UserName = my_name;

                    // 如果登陆成功且用户选择记住密码  则将用户信息填入SharedPreference中
                    if(isSavedFlag){
                        editor.putString("name",my_name);
                        editor.putString("pswd",my_pswd);
                    }
                    // 存储用户记住密码状态标志
                    editor.putBoolean("flag", isSavedFlag);
                    // 提交数据
                    editor.commit();
                    // 发送显示Intent  切换到ActionBarActivity
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败，用户名或密码输入不正确！", Toast.LENGTH_SHORT).show();
                }
            }

            // 连接失败的处理
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
}
