package netRequest;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils
{
    //将输入流转化为字节数组
    public static byte[] ConvertInputStreamToBytes(InputStream is)throws Exception
    {
        byte[] bytes=new byte[1024];
        byte[] desbytes=new byte[1024];
        int length=0;
        int desloc=0;
        int lastlength=0;
        int k=0;
        while ((length=is.read(bytes))!=-1)
        {
            System.arraycopy(bytes,0,desbytes,desloc,length);
            byte[] temp=new byte[desbytes.length+1024];
            System.arraycopy(desbytes,0,temp,0,desbytes.length);
            desbytes=temp;
            k=desloc;
            desloc=desloc+length;
            lastlength=length;
        }
        bytes=new byte[k+lastlength];
        System.arraycopy(desbytes, 0, bytes, 0, bytes.length);
        is.close();
        desbytes=null;
        is=null;
        return bytes;
    }


    //  利用Java 反射机制 分析类
    //将request转化为NetTopRequest这样的基本请求单元
    public static NetTopRequest parseDataToNetTopRequest(Request request)
    {
        // 因为有5种Request的形式 需要进行分析

        // Java 反射机制  getClass()方法返回一个Class类型的实例  判断是五种Request类型中的哪一类型
        Class cl=request.getClass();

        // java.lang.reflect包中的Field 用于描述类的域
        //getDeclaredFields() 返回包含Field对象的数组，这些对象记录了这个类的全部域
        // 其中包括私有和受保护成员的数组
        Field[] fields=cl.getDeclaredFields();


        Map<String,String> dataParams=new HashMap<>();
        String requestUrl=null;
        File[] files=null;
        for(int i=0;i<fields.length;i++)
        {
            // 为反射对象设置可访问标志
            // flag为true表明屏蔽Java语言的访问检查，使得对象的私有属性也可以被查询和设置
            fields[i].setAccessible(true);
            try
            {
                // 判断域是Url还是files
                if(fields[i].getName().equals("requestUrl"))
                {
                    requestUrl=fields[i].get(request).toString();
                }
                else if(fields[i].getName().equals("files"))
                {
                    files=(File[])fields[i].get(request);
                }
                else
                {
                    System.out.println("params="+fields[i].get(request).toString()+"  "+fields[i].getName());
                    dataParams.put(fields[i].getName(),fields[i].get(request).toString());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return new NetTopRequest(requestUrl,dataParams,files);
    }
}
