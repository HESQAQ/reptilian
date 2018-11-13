package com.zpy.spider;

import org.apache.http.protocol.HTTP;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liaog
 * @Date: 2018-08-01 13:28
 * @description:
 */
public class JavaHttpClient extends AbstractHttpClient implements HttpClient{

    protected String defaultCharset = "utf-8";

    protected int timeout = 20000;

    protected boolean useProxy;

    protected String hostname;

    protected int port;

    public HttpResponse doRequest(HttpMethod method, String url, Map<String, String> userHeaders, InputStream data,Map<String, String> cookies) throws IOException {

        URL urlObject = new URL(url);
        HttpURLConnection urlConnection = openConnection(urlObject);

        if (urlObject.getProtocol().equalsIgnoreCase("https")) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
            prepareForHttps(httpsURLConnection);
        }
        String sessionid="";
        if (cookies != null) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                //urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
                sessionid=sessionid+entry.getKey()+"="+entry.getValue()+";";
            }
            urlConnection.addRequestProperty("Cookie", sessionid);
        }

        urlConnection.setRequestMethod(method.name());
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setReadTimeout(timeout);
        urlConnection.setInstanceFollowRedirects(false);
        if (userHeaders != null) {
            for (Map.Entry<String, String> entry : userHeaders.entrySet()) {
                urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (data != null) {
            int len = data.available();
            urlConnection.addRequestProperty("Content-Length", String.valueOf(len));
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            IOUtil.copyAndClose(data, outputStream);
        }
        urlConnection.setInstanceFollowRedirects( false );
        InputStream responseInputStream = urlConnection.getInputStream();
        int responseCode = urlConnection.getResponseCode();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
        IOUtil.copyAndClose(responseInputStream, bos);

        Map<String, List<String>> headers = new HashMap<String, List<String>>(urlConnection.getHeaderFields());
        String sessionId = "";
        String cookieVal = "";
        String key = null;
        Map<String,String > map=new HashMap();
        for(int i = 1; (key = urlConnection.getHeaderFieldKey(i)) != null; i++){//获取cookies
            if(key.equalsIgnoreCase("set-cookie")){
                cookieVal = urlConnection.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                String s[]=cookieVal.split("=");
                map.put(s[0],s[1]);
                sessionId = sessionId + cookieVal + ";";
                System.out.println("==="+cookieVal);
            }
        }
       // System.out.println("session"+sessionId);
        String redirect=urlConnection.getHeaderField( "location" );//获得302转发地址
        //System.out.println("location:"+redirect);
        /*if(redirect!=null){
            doRequest(HttpMethod.GET,redirect,userHeaders,null,map);
        }*/

        return new HttpResponse(defaultCharset, responseCode, headers, bos.toByteArray(),map);
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        if (isUseProxy()) {
            return (HttpURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getHostname(), getPort())));
        } else {
            return (HttpURLConnection)url.openConnection();
        }
    }

    private void prepareForHttps(HttpsURLConnection httpsURLConnection) {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new AbstractHttpClient.TrustAnyTrustManager()}, secureRandom);
            httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
