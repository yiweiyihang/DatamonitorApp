package netRequest;

/**
 * Created by 32618 on 2016/12/23.
 */

public interface NetTopListener {
    public void onSuccess(HttpResponse response);
    public void onFail();
    public void onError();
}
