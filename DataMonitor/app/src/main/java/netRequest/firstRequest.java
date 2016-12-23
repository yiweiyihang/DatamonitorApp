package netRequest;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * 用于发送数据库中user表的操作申请
 * 检查用户是否有权限登录  以及 用户注册
 */
public class firstRequest implements Request
{
    public String requestUrl = "http://"+ Constants.Url+"/MyProject/CheckServlet";
    public String uname;
    public String upwd;
    public String flag;   // 用来标志  登录or注册 操作
}