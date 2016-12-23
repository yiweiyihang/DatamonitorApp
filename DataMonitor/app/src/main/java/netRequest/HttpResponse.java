package netRequest;

/**
 * 返回服务器的回复
 */

public class HttpResponse
{
    public byte[] bytes;
    public HttpResponse(byte[] bytes)
    {
        this.bytes=bytes;
    }
}
