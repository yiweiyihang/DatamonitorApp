package netRequest;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * Created by 32618 on 2016/12/25.
 */

public class DataRequest implements Request{

    public String requestUrl = "http://"+ Constants.Url+"/MyProject/DataReqServlet";
    public String dataReq ;//包装好请求数据信息包括工序id和测点id用json包装
}