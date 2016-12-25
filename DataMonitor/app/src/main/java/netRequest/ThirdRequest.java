package netRequest;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * Created by 32618 on 2016/12/24.
 */

public class ThirdRequest implements Request{
    public String requestUrl = "http://"+ Constants.Url+"/MyProject/GetParaServlet";
    public String proID;
}
