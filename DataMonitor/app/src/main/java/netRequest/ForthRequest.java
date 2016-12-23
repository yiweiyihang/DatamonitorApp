package netRequest;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * 发送修改用户密码的申请
 */
public class ForthRequest implements Request {
    public String requestUrl = "http://"+ Constants.Url+"/MyProject/Changepswd";
    public String orgpwd;
    public String newpwd;
    public String username;
}

