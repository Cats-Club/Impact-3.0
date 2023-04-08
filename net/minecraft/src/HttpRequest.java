package net.minecraft.src;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest
{
    private String host = null;
    private int port = 0;
    private Proxy proxy = Proxy.NO_PROXY;
    private String method = null;
    private String file = null;
    private String http = null;
    private Map<String, String> headers = new LinkedHashMap();
    private byte[] body = null;
    private int redirects = 0;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";

    public HttpRequest(String p_i49_1_, int p_i49_2_, Proxy p_i49_3_, String p_i49_4_, String p_i49_5_, String p_i49_6_, Map<String, String> p_i49_7_, byte[] p_i49_8_)
    {
        this.host = p_i49_1_;
        this.port = p_i49_2_;
        this.proxy = p_i49_3_;
        this.method = p_i49_4_;
        this.file = p_i49_5_;
        this.http = p_i49_6_;
        this.headers = p_i49_7_;
        this.body = p_i49_8_;
    }

    public String getHost()
    {
        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getMethod()
    {
        return this.method;
    }

    public String getFile()
    {
        return this.file;
    }

    public String getHttp()
    {
        return this.http;
    }

    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    public byte[] getBody()
    {
        return this.body;
    }

    public int getRedirects()
    {
        return this.redirects;
    }

    public void setRedirects(int p_setRedirects_1_)
    {
        this.redirects = p_setRedirects_1_;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }
}
