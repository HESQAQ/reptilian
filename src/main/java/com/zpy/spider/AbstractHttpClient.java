package com.zpy.spider;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @Author: liaogk
 * @Date: 2018-08-01 13:19
 * @description:
 */
public abstract class AbstractHttpClient implements HttpClient{

    @Override
    public abstract HttpResponse doRequest(HttpMethod method, String url, Map<String, String> userHeaders, InputStream data ,Map<String,String> cookies) throws IOException;

    @Override
    public  byte[] doGet(String url) throws IOException {
        return doGet(url, null).getResponseData();
    }

    @Override
    public HttpResponse doGet(String url, Map<String, String> headers) throws IOException {
        return doRequest(HttpMethod.GET, url, headers, null,null);
    }


    @Override
    public HttpResponse doGet2(String url, Map<String, String> headers,Map<String,String> cookies) throws IOException {
        return doRequest(HttpMethod.GET, url, headers, null,cookies);
    }

    @Override
    public HttpResponse dopost(String url, Map<String, String> headers,InputStream data, Map<String, String>  cookies) throws IOException {
        return doRequest(HttpMethod.POST, url, headers, data,cookies);
    }
    protected static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    protected static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    protected final SecureRandom secureRandom = new SecureRandom();
}
