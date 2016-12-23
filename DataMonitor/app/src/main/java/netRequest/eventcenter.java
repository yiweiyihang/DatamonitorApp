package netRequest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by novas on 16/3/3.
 * 根据Http的返回结果进行相应的判断
 */
public class eventcenter {
    public static eventcenter center=null;
    Hashtable<Object,NetTopListener> listenerHashtable=new Hashtable<>();
    Handler mHandler=new Handler(Looper.getMainLooper()) {  // Java 线程应用  待看
        @Override
        public void handleMessage(Message msg) {
            System.out.println("receive"+ Looper.myLooper()+"   "+Looper.getMainLooper());
            if(msg.what==1)
            {
                System.out.println("receive");
                HttpResponse response=(HttpResponse)msg.obj;
                NetTopListener listener=listenerHashtable.get(response);
                listener.onSuccess(response);
                listenerHashtable.remove(response);
            }
            else if(msg.what==0)
            {
                Object obj=msg.obj;
                NetTopListener listener=listenerHashtable.get(obj);
                listener.onFail();
                listenerHashtable.remove(obj);
            }
            else if(msg.what==-1)
            {
                Object obj=msg.obj;
                NetTopListener listener=listenerHashtable.get(obj);
                listener.onFail();
                listenerHashtable.remove(obj);
            }
        }
    };
    private eventcenter()
    {

    }
    public  void postError(Object object,NetTopListener listener)
    {
        listenerHashtable.put(object,listener);
        mHandler.obtainMessage(-1,object).sendToTarget();
    }
    public  void postFail(Object object,NetTopListener listener)
    {
        listenerHashtable.put(object,listener);
        mHandler.obtainMessage(0,object).sendToTarget();
    }
    public  void postSuccess(HttpResponse response,NetTopListener listener)
    {
        System.out.println("in put table");
        listenerHashtable.put(response,listener);
        mHandler.obtainMessage(1,response).sendToTarget();
    }

    public static eventcenter getCenterInstance()
    {
        if(center==null)
        {
            center=new eventcenter();
        }
        return center;
    }

    private static ExecutorService executorService= Executors.newCachedThreadPool();

    public void execute(Runnable runnable)
    {
        executorService.execute(runnable);
    }
}
