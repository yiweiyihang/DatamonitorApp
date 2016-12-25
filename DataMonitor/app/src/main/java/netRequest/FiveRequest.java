package netRequest;

import com.yiweiyihangft.datamonitor.Constants;

/**
 * Created by 32618 on 2016/12/25.
 */
public class FiveRequest implements Request{
    public String requestUrl = "http://"+ Constants.Url+"/MyProject/LineChartServlet";
    public String proID;
    public String timeSpace;
    public String paraID;
}
